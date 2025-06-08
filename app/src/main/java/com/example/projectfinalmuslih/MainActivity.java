package com.example.projectfinalmuslih;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeagueAdapter adapter;
    private List<League> leagueList = new ArrayList<>();
    private ApiService apiService;
    private SharedPrefManager prefManager;

    // 🔁 Background Thread
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Tema diset dari SharedPreferences
        prefManager = new SharedPrefManager(this);
        if (prefManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔁 UI Mode Toggle (Switch)
        Switch switchTheme = findViewById(R.id.switchTheme);
        switchTheme.setChecked(prefManager.isDarkMode());
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefManager.setDarkMode(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate(); // Refresh activity
        });

        // 🔁 Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LeagueAdapter(leagueList, this::onLeagueClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        fetchLeagues(); // Ambil data liga dari API
    }

    private void onLeagueClick(League league) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("league_name", league.getName()); // kirim nama liga
        startActivity(intent);
    }


    // 🔁 Mengambil data liga dari API menggunakan background thread
    private void fetchLeagues() {
        executor.execute(() -> {
            Call<LeagueResponse> call = apiService.getLeagues();
            try {
                Response<LeagueResponse> response = call.execute();
                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        leagueList.clear();
                        leagueList.addAll(response.body().getResponse());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(MainActivity.this, "Jaringan gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}

package com.example.projectfinalmuslih;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnRefresh;
    private TeamAdapter adapter;
    private List<Team> teamList = new ArrayList<>();
    private ApiService apiService;
    private String leagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = findViewById(R.id.recyclerView);
        btnRefresh = findViewById(R.id.btnRefresh);

        adapter = new TeamAdapter(teamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        leagueName = getIntent().getStringExtra("league_name");

        if (leagueName != null) {
            fetchTeams();
        } else {
            Toast.makeText(this, "Nama liga tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        btnRefresh.setOnClickListener(v -> fetchTeams());
    }

    private void fetchTeams() {
        btnRefresh.setEnabled(false);
        apiService.getTeams(leagueName).enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                btnRefresh.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    teamList.clear();
                    teamList.addAll(response.body().getResponse());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DetailActivity.this, "Gagal mengambil data tim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                btnRefresh.setEnabled(true);
                Toast.makeText(DetailActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

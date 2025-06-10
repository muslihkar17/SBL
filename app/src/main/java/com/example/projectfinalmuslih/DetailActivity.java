package com.example.projectfinalmuslih;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinalmuslih.data.model.Team;
import com.example.projectfinalmuslih.data.model.TeamResponse;
import com.example.projectfinalmuslih.adapter.TeamAdapter;
import com.example.projectfinalmuslih.data.network.ApiClient;
import com.example.projectfinalmuslih.data.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnRefresh;
    private TextView detailTitle;
    private TeamAdapter adapter;
    private ApiService apiService;
    private String leagueId; // <-- PERUBAHAN 1: Tipe data diubah ke String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = findViewById(R.id.recyclerView);
        btnRefresh = findViewById(R.id.btnRefresh);
        detailTitle = findViewById(R.id.detailTitle);

        adapter = new TeamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // --- PERUBAHAN 2: Cara mengambil data dari Intent ---
        leagueId = getIntent().getStringExtra("league_id");
        String leagueName = getIntent().getStringExtra("league_name");

        if (leagueName != null) {
            detailTitle.setText("Daftar Tim di " + leagueName);
        }

        if (leagueId != null && !leagueId.isEmpty()) {
            fetchTeams();
        } else {
            Toast.makeText(this, "ID liga tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        btnRefresh.setOnClickListener(v -> fetchTeams());
    }

    private void fetchTeams() {
        btnRefresh.setEnabled(false);
        // Pemanggilan ini sekarang sudah benar karena leagueId adalah String
        apiService.getTeams(leagueId).enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                btnRefresh.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    List<Team> fetchedTeams = response.body().getTeams();
                    if (fetchedTeams != null) {
                        adapter.submitList(fetchedTeams);
                    } else {
                        Toast.makeText(DetailActivity.this, "Tidak ada data tim untuk liga ini", Toast.LENGTH_SHORT).show();
                    }
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
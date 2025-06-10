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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnRefresh;
    private TextView detailTitle; // Tambahkan ini untuk judul
    private TeamAdapter adapter;
    private List<Team> teamList = new ArrayList<>();
    private ApiService apiService;
    private int leagueId; // Gunakan tipe data int untuk ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = findViewById(R.id.recyclerView);
        btnRefresh = findViewById(R.id.btnRefresh);
        detailTitle = findViewById(R.id.detailTitle); // Inisialisasi TextView judul

        adapter = new TeamAdapter(teamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Ambil ID dan NAMA dari Intent yang dikirim MainActivity
        leagueId = getIntent().getIntExtra("league_id", -1);
        String leagueName = getIntent().getStringExtra("league_name");

        // Atur judul halaman agar dinamis
        if (leagueName != null) {
            detailTitle.setText("Daftar Tim di " + leagueName);
        }

        // Lakukan fetch data jika ID liga valid
        if (leagueId != -1) {
            fetchTeams();
        } else {
            Toast.makeText(this, "ID liga tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        btnRefresh.setOnClickListener(v -> fetchTeams());
    }

    private void fetchTeams() {
        btnRefresh.setEnabled(false);
        // Panggil API dengan ID liga
        apiService.getTeams(leagueId).enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                btnRefresh.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    // Gunakan metode getTeams() dari TeamResponse yang sudah diperbaiki
                    List<Team> fetchedTeams = response.body().getTeams();
                    if (fetchedTeams != null) {
                        teamList.clear();
                        teamList.addAll(fetchedTeams);
                        adapter.notifyDataSetChanged();
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
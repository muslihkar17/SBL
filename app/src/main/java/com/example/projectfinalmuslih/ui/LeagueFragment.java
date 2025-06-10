package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.adapter.LeagueAdapter;
import com.example.projectfinalmuslih.data.local.AppDatabase;
import com.example.projectfinalmuslih.data.model.League;
import com.example.projectfinalmuslih.data.model.LeagueResponse;
import com.example.projectfinalmuslih.data.network.ApiClient;
import com.example.projectfinalmuslih.data.network.ApiService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class LeagueFragment extends Fragment {

    // Deklarasi Variabel
    private RecyclerView recyclerView;
    private LeagueAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;

    private ApiService apiService;
    private AppDatabase appDatabase;
    private ExecutorService executor;
    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inisialisasi komponen non-view
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        apiService = ApiClient.getClient().create(ApiService.class);

        // Inisialisasi Database
        appDatabase = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "sports-db")
                .fallbackToDestructiveMigration() // Hati-hati: ini akan menghapus DB jika skema berubah
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        view = inflater.inflate(R.layout.fragment_league, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Atur judul Action Bar
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Daftar Liga");
        }

        // Inisialisasi Views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi Adapter
        adapter = new LeagueAdapter();
        recyclerView.setAdapter(adapter);

        // Atur listener untuk klik item, navigasi ke daftar tim
        adapter.setOnItemClickListener(league -> {
            LeagueFragmentDirections.ActionLeagueFragmentToTeamFragment action =
                    LeagueFragmentDirections.actionLeagueFragmentToTeamFragment(league.idLeague, league.strLeague);
            Navigation.findNavController(view).navigate(action);
        });

        // Atur listener untuk Swipe-to-Refresh
        swipeRefreshLayout.setOnRefreshListener(this::fetchLeagues);

        // Panggil data untuk pertama kali
        fetchLeagues();
    }

    private void fetchLeagues() {
        swipeRefreshLayout.setRefreshing(true);
        executor.execute(() -> {
            try {
                // 1. Panggil API untuk mendapatkan data terbaru
                Response<LeagueResponse> response = apiService.getLeagues().execute();
                if (response.isSuccessful() && response.body() != null) {
                    // 2. Jika sukses, simpan data ke database Room
                    appDatabase.leagueDao().insertAll(response.body().getLeagues());
                }
            } catch (IOException e) {
                // 3. Jika API gagal (misal: tidak ada internet), tampilkan pesan
                handler.post(() -> Toast.makeText(getContext(), "Gagal mengambil data baru, menampilkan data offline.", Toast.LENGTH_SHORT).show());
            } finally {
                // 4. Selalu ambil data dari database untuk ditampilkan di UI
                List<League> leaguesFromDb = appDatabase.leagueDao().getAllLeagues();
                handler.post(() -> {
                    if (leaguesFromDb != null && !leaguesFromDb.isEmpty()) {
                        adapter.submitList(leaguesFromDb);
                    } else {
                        // Tampilkan pesan jika database juga kosong
                        Toast.makeText(getContext(), "Tidak ada data yang tersedia.", Toast.LENGTH_SHORT).show();
                    }
                    // Hentikan animasi refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
}
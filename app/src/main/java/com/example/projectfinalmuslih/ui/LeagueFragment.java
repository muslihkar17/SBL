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
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        apiService = ApiClient.getClient().create(ApiService.class);

        appDatabase = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "sports-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_league, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Daftar Liga");
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeagueAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(league -> {
            LeagueFragmentDirections.ActionLeagueFragmentToTeamFragment action =
                    LeagueFragmentDirections.actionLeagueFragmentToTeamFragment(league.idLeague, league.strLeague);
            Navigation.findNavController(view).navigate(action);
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchLeagues);

        fetchLeagues();
    }

    private void fetchLeagues() {
        swipeRefreshLayout.setRefreshing(true);
        executor.execute(() -> {
            try {
                Response<LeagueResponse> response = apiService.getLeagues().execute();
                if (response.isSuccessful() && response.body() != null) {
                    appDatabase.leagueDao().insertAll(response.body().getLeagues());
                }
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(getContext(), "Gagal mengambil data baru, menampilkan data offline.", Toast.LENGTH_SHORT).show());
            } finally {
                List<League> leaguesFromDb = appDatabase.leagueDao().getAllLeagues();
                handler.post(() -> {
                    if (leaguesFromDb != null && !leaguesFromDb.isEmpty()) {
                        adapter.submitList(leaguesFromDb);
                    } else {
                        Toast.makeText(getContext(), "Tidak ada data yang tersedia.", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
}
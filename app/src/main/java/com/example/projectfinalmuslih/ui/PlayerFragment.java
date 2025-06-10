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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.adapter.PlayerAdapter;
import com.example.projectfinalmuslih.data.local.AppDatabase;
import com.example.projectfinalmuslih.data.model.Player;
import com.example.projectfinalmuslih.data.model.PlayerResponse;
import com.example.projectfinalmuslih.data.network.ApiClient;
import com.example.projectfinalmuslih.data.network.ApiService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class PlayerFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private int teamId;
    private String teamName;

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

        if (getArguments() != null) {
            teamId = PlayerFragmentArgs.fromBundle(getArguments()).getTeamId();
            teamName = PlayerFragmentArgs.fromBundle(getArguments()).getTeamName();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Toolbar
        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            NavController navController = Navigation.findNavController(view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        }

        if (teamName != null) {
            toolbar.setTitle("Pemain di " + teamName);
        }

        // Find views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PlayerAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::fetchPlayers);

        fetchPlayers();
    }

    private void fetchPlayers() {
        swipeRefreshLayout.setRefreshing(true);
        executor.execute(() -> {
            try {
                Response<PlayerResponse> response = apiService.getPlayers(teamId).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Player> players = response.body().getPlayers();
                    if (players != null) {
                        for(Player player : players) {
                            player.idTeam = teamId; // Memastikan setiap pemain memiliki ID tim
                        }
                        appDatabase.playerDao().insertAll(players);
                    }
                }
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(getContext(), "Gagal mengambil data baru, menampilkan data offline.", Toast.LENGTH_SHORT).show());
            } finally {
                List<Player> playersFromDb = appDatabase.playerDao().getPlayersByTeam(teamId);
                handler.post(() -> {
                    if (playersFromDb != null && !playersFromDb.isEmpty()) {
                        adapter.submitList(playersFromDb);
                    } else {
                        Toast.makeText(getContext(), "Tidak ada data pemain yang tersedia.", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
}
package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.projectfinalmuslih.adapter.TeamAdapter;
import com.example.projectfinalmuslih.data.local.AppDatabase;
import com.example.projectfinalmuslih.data.model.Team;
import com.example.projectfinalmuslih.data.model.TeamResponse;
import com.example.projectfinalmuslih.data.network.ApiClient;
import com.example.projectfinalmuslih.data.network.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TeamFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText searchEditText;

    private ApiService apiService;
    private AppDatabase appDatabase;
    private ExecutorService executor;
    private Handler handler;
    private List<Team> allTeams = new ArrayList<>();
    private int leagueId;
    private String leagueName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            leagueId = TeamFragmentArgs.fromBundle(getArguments()).getLeagueId();
            leagueName = TeamFragmentArgs.fromBundle(getArguments()).getLeagueName();
        }

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        apiService = ApiClient.getClient().create(ApiService.class);
        appDatabase = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "sports-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            NavController navController = Navigation.findNavController(view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        }
        toolbar.setTitle(leagueName);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TeamAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(team -> {
            TeamFragmentDirections.ActionTeamFragmentToPlayerFragment action =
                    TeamFragmentDirections.actionTeamFragmentToPlayerFragment(team.idTeam, team.strTeam);
            Navigation.findNavController(view).navigate(action);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTeams(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchTeams);
        fetchTeams();
    }

    private void fetchTeams() {
        swipeRefreshLayout.setRefreshing(true);
        executor.execute(() -> {
            try {
                Response<TeamResponse> response = apiService.getTeams(leagueId).execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Team> teams = response.body().getTeams();
                    if (teams != null) {
                        for(Team team : teams) {
                            team.idLeague = leagueId;
                        }
                        appDatabase.teamDao().insertAll(teams);
                    }
                }
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(getContext(), "Gagal mengambil data baru, menampilkan data offline.", Toast.LENGTH_SHORT).show());
            } finally {
                List<Team> teamsFromDb = appDatabase.teamDao().getTeamsByLeague(leagueId);
                handler.post(() -> {
                    allTeams.clear();
                    if (teamsFromDb != null && !teamsFromDb.isEmpty()) {
                        allTeams.addAll(teamsFromDb);
                    } else {
                        Toast.makeText(getContext(), "Tidak ada data tim yang tersedia.", Toast.LENGTH_SHORT).show();
                    }
                    adapter.submitList(new ArrayList<>(allTeams));
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    private void filterTeams(String text) {
        List<Team> filteredList = new ArrayList<>();
        for (Team team : allTeams) {
            if (team.strTeam.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(team);
            }
        }
        adapter.submitList(filteredList);
    }
}
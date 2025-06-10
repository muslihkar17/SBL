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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.adapter.LeagueAdapter;
import com.example.projectfinalmuslih.data.model.League;
import com.example.projectfinalmuslih.data.model.LeagueResponse;
import com.example.projectfinalmuslih.data.network.ApiClient;
import com.example.projectfinalmuslih.data.network.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class LeagueFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeagueAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText searchEditText;

    private ApiService apiService;
    private ExecutorService executor;
    private Handler handler;
    private List<League> allLeagues = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_league, container, false);
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
        toolbar.setTitle("Daftar Liga");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeagueAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(league -> {
            // Debugger: Log ID yang dikirim dari LeagueFragment
            android.util.Log.d("ID_TRACE", "LeagueFragment MENGIRIM ID: " + league.idLeague + " (" + league.strLeague + ")");

            LeagueFragmentDirections.ActionLeagueFragmentToTeamFragment action =
                    LeagueFragmentDirections.actionLeagueFragmentToTeamFragment(league.idLeague, league.strLeague);
            Navigation.findNavController(view).navigate(action);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLeagues(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchLeagues);
        fetchLeagues();
    }

    private void fetchLeagues() {
        swipeRefreshLayout.setRefreshing(true);
        executor.execute(() -> {
            try {
                Response<LeagueResponse> response = apiService.getLeagues().execute();
                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        List<League> fetchedLeagues = response.body().getLeagues();
                        allLeagues.clear();
                        if (fetchedLeagues != null && !fetchedLeagues.isEmpty()) {
                            allLeagues.addAll(fetchedLeagues);
                        } else {
                            Toast.makeText(getContext(), "Tidak ada data liga yang tersedia dari API.", Toast.LENGTH_SHORT).show();
                        }
                        adapter.submitList(new ArrayList<>(allLeagues));
                    } else {
                        Toast.makeText(getContext(), "Gagal mengambil data liga dari API: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
            } catch (IOException e) {
                handler.post(() -> {
                    Toast.makeText(getContext(), "Koneksi gagal atau terjadi error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    adapter.submitList(new ArrayList<>());
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    private void filterLeagues(String text) {
        List<League> filteredList = new ArrayList<>();
        for (League league : allLeagues) {
            if (league.strLeague.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(league);
            }
        }
        adapter.submitList(filteredList);
    }
}
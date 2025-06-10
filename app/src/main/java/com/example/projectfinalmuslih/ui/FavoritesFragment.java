// app/src/main/java/com/example/projectfinalmuslih/ui/FavoritesFragment.java
package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.adapter.TeamAdapter; // Use TeamAdapter for favorites
import com.example.projectfinalmuslih.data.local.AppDatabase;
import com.example.projectfinalmuslih.data.local.TeamDao;
import com.example.projectfinalmuslih.data.model.Team;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamAdapter adapter;
    private TeamDao teamDao;
    private TextView emptyFavoritesTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamDao = AppDatabase.getDatabase(requireContext()).teamDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar_favorites);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            NavController navController = Navigation.findNavController(view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        }
        toolbar.setTitle("Tim Favorit");


        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyFavoritesTextView = view.findViewById(R.id.emptyFavoritesTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TeamAdapter(); // Reusing TeamAdapter
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(team -> {
            Bundle bundle = new Bundle();
            bundle.putInt("teamId", team.idTeam); // Pass ID as well
            bundle.putString("teamName", team.strTeam);
            bundle.putString("teamBadgeUrl", team.strTeamBadge);
            bundle.putString("teamDescription", team.strDescriptionEN);
            bundle.putString("leagueId", team.idLeague); // Pass leagueId
            Navigation.findNavController(view).navigate(R.id.action_favoritesFragment_to_teamDetailFragment, bundle);
        });

        // Observe changes in favorite teams
        teamDao.getAllFavoriteTeams().observe(getViewLifecycleOwner(), new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                adapter.submitList(teams);
                if (teams == null || teams.isEmpty()) {
                    emptyFavoritesTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyFavoritesTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
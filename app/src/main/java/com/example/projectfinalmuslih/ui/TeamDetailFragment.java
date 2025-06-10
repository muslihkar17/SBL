// app/src/main/java/com/example/projectfinalmuslih/ui/TeamDetailFragment.java
package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.data.local.AppDatabase;
import com.example.projectfinalmuslih.data.local.TeamDao;
import com.example.projectfinalmuslih.data.model.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeamDetailFragment extends Fragment {

    private FloatingActionButton fabFavorite;
    private Team currentTeam;
    private TeamDao teamDao;
    private ExecutorService executorService;
    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamDao = AppDatabase.getDatabase(requireContext()).teamDao();
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar_detail);
        androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        ImageView teamBadge = view.findViewById(R.id.teamDetailBadge);
        TextView teamName = view.findViewById(R.id.teamDetailName);
        TextView teamDescription = view.findViewById(R.id.teamDetailDescription);
        fabFavorite = view.findViewById(R.id.fab_favorite);

        if (getArguments() != null) {
            String name = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamName();
            String badgeUrl = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamBadgeUrl();
            String description = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamDescription();
            int teamId = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamId();
            String leagueId = TeamDetailFragmentArgs.fromBundle(getArguments()).getLeagueId();

            currentTeam = new Team();
            currentTeam.idTeam = teamId;
            currentTeam.strTeam = name;
            currentTeam.strTeamBadge = badgeUrl;
            currentTeam.strDescriptionEN = description;
            currentTeam.idLeague = leagueId; // Set the leagueId for the team

            if (name != null) {
                toolbar.setTitle(name);
            }

            teamName.setText(name);
            teamDescription.setText(description);

            Glide.with(this)
                    .load(badgeUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(teamBadge);

            // Check if team is favorite and update FAB icon
            checkFavoriteStatus();
        }

        fabFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void checkFavoriteStatus() {
        if (currentTeam == null) return;

        executorService.execute(() -> {
            boolean isFavorite = teamDao.isTeamFavorite(currentTeam.idTeam);
            handler.post(() -> {
                if (isFavorite) {
                    fabFavorite.setImageResource(R.drawable.ic_favorites_filled); // Assume you have a filled favorite icon
                } else {
                    fabFavorite.setImageResource(R.drawable.ic_favorites); // Existing favorite icon
                }
            });
        });
    }

    private void toggleFavorite() {
        if (currentTeam == null) return;

        executorService.execute(() -> {
            boolean isFavorite = teamDao.isTeamFavorite(currentTeam.idTeam);
            if (isFavorite) {
                teamDao.deleteTeam(currentTeam.idTeam);
                handler.post(() -> {
                    fabFavorite.setImageResource(R.drawable.ic_favorites);
                    Toast.makeText(getContext(), currentTeam.strTeam + " dihapus dari favorit", Toast.LENGTH_SHORT).show();
                });
            } else {
                teamDao.insertTeam(currentTeam);
                handler.post(() -> {
                    fabFavorite.setImageResource(R.drawable.ic_favorites_filled);
                    Toast.makeText(getContext(), currentTeam.strTeam + " ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
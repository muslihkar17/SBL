package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.projectfinalmuslih.R;

public class TeamDetailFragment extends Fragment {

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

        androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(view);
        androidx.navigation.ui.AppBarConfiguration appBarConfiguration =
                new androidx.navigation.ui.AppBarConfiguration.Builder(navController.getGraph()).build();
        androidx.navigation.ui.NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        ImageView teamBadge = view.findViewById(R.id.teamDetailBadge);
        TextView teamName = view.findViewById(R.id.teamDetailName);
        TextView teamDescription = view.findViewById(R.id.teamDetailDescription);

        if (getArguments() != null) {
            String name = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamName();
            String badgeUrl = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamBadgeUrl();
            String description = TeamDetailFragmentArgs.fromBundle(getArguments()).getTeamDescription();

            // Debugger: Log detail tim yang diterima
            android.util.Log.d("ID_TRACE", "TeamDetailFragment MENERIMA: Nama=" + name + ", Badge URL=" + badgeUrl);

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
        }
    }
}
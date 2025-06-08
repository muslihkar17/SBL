package com.example.projectfinalmuslih;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private List<Team> teams;

    public TeamAdapter(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.name.setText(team.getName());
        // Load logo menggunakan Glide atau Picasso
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamName);
            logo = itemView.findViewById(R.id.teamLogo);
        }
    }
}


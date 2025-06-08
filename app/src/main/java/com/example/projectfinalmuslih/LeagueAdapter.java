package com.example.projectfinalmuslih;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.ViewHolder> {
    private List<League> leagues;
    private OnLeagueClickListener listener;

    public interface OnLeagueClickListener {
        void onLeagueClick(League league);
    }

    public LeagueAdapter(List<League> leagues, OnLeagueClickListener listener) {
        this.leagues = leagues;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        League league = leagues.get(position);
        holder.name.setText(league.getName());
        // Load logo menggunakan Glide atau Picasso
        holder.itemView.setOnClickListener(v -> listener.onLeagueClick(league));
    }

    @Override
    public int getItemCount() {
        return leagues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.leagueName);
            logo = itemView.findViewById(R.id.leagueLogo);
        }
    }
}


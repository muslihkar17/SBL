package com.example.projectfinalmuslih.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.data.model.League;

public class LeagueAdapter extends ListAdapter<League, LeagueAdapter.LeagueViewHolder> {

    private OnItemClickListener listener;

    public LeagueAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<League> DIFF_CALLBACK = new DiffUtil.ItemCallback<League>() {
        @Override
        public boolean areItemsTheSame(@NonNull League oldItem, @NonNull League newItem) {
            return oldItem.idLeague == newItem.idLeague;
        }

        @Override
        public boolean areContentsTheSame(@NonNull League oldItem, @NonNull League newItem) {
            return oldItem.strLeague.equals(newItem.strLeague);
        }
    };

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        League league = getItem(position);
        holder.bind(league);
    }

    class LeagueViewHolder extends RecyclerView.ViewHolder {
        ImageView leagueLogo;
        TextView leagueName;

        LeagueViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueLogo = itemView.findViewById(R.id.leagueLogo);
            leagueName = itemView.findViewById(R.id.leagueName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        void bind(League league) {
            leagueName.setText(league.strLeague);
            Glide.with(itemView.getContext())
                    .load(league.strBadge) // atau strLogo
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(leagueLogo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(League league);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
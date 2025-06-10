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
import com.example.projectfinalmuslih.data.model.Team;

public class TeamAdapter extends ListAdapter<Team, TeamAdapter.TeamViewHolder> {

    private OnItemClickListener listener;

    public TeamAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Team> DIFF_CALLBACK = new DiffUtil.ItemCallback<Team>() {
        @Override
        public boolean areItemsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.idTeam == newItem.idTeam;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.strTeam.equals(newItem.strTeam);
        }
    };

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = getItem(position);
        holder.bind(team);
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView teamLogo;
        TextView teamName;

        TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamLogo = itemView.findViewById(R.id.teamLogo);
            teamName = itemView.findViewById(R.id.teamName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        // Di dalam kelas TeamAdapter.java > TeamViewHolder
        void bind(Team team) {
            teamName.setText(team.strTeam);

            // Kita tetap sertakan log ini untuk memastikan URL tidak null
            android.util.Log.d("TeamAdapterDebug", "Memproses tim: " + team.strTeam + " | URL Logo: " + team.strTeamBadge);

            Glide.with(itemView.getContext())
                    .load(team.strTeamBadge)
                    .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e, @androidx.annotation.Nullable Object model, @androidx.annotation.NonNull com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                            // Jika GAGAL, kita catat error lengkapnya ke Logcat
                            android.util.Log.e("TeamAdapterDebug", "Glide GAGAL memuat gambar. URL: " + model, e);
                            return false; // return false agar placeholder/error drawable tetap ditampilkan
                        }

                        @Override
                        public boolean onResourceReady(@androidx.annotation.NonNull android.graphics.drawable.Drawable resource, @androidx.annotation.NonNull Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, @androidx.annotation.NonNull com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            // Jika BERHASIL, kita catat juga sebagai konfirmasi
                            android.util.Log.d("TeamAdapterDebug", "Glide BERHASIL memuat gambar dari URL: " + model);
                            return false; // return false agar Glide melanjutkan proses menampilkan gambar ke ImageView
                        }
                    })
                    .placeholder(R.drawable.ic_launcher_background)
                    .fallback(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(teamLogo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Team team);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
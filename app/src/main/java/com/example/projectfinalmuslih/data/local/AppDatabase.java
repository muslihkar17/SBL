package com.example.projectfinalmuslih.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.projectfinalmuslih.data.model.League;
import com.example.projectfinalmuslih.data.model.Team;
import com.example.projectfinalmuslih.data.model.Player;

@Database(entities = {League.class, Team.class, Player.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LeagueDao leagueDao();
    public abstract TeamDao teamDao();
    public abstract PlayerDao playerDao();
}
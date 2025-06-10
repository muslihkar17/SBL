package com.example.projectfinalmuslih.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.projectfinalmuslih.data.model.League;
import com.example.projectfinalmuslih.data.model.Team;

@Database(entities = {League.class, Team.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LeagueDao leagueDao();
    public abstract TeamDao teamDao();
}
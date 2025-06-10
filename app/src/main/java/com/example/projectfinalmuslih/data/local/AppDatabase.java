// app/src/main/java/com/example/projectfinalmuslih/data/local/AppDatabase.java
package com.example.projectfinalmuslih.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;

import com.example.projectfinalmuslih.data.model.League;
import com.example.projectfinalmuslih.data.model.Team;

@Database(entities = {League.class, Team.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LeagueDao leagueDao();
    public abstract TeamDao teamDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "sbl_database")
                            .fallbackToDestructiveMigration() // Allows for schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
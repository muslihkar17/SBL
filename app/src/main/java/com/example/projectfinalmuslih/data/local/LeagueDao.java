package com.example.projectfinalmuslih.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.projectfinalmuslih.data.model.League;
import java.util.List;

@Dao
public interface LeagueDao {
    @Query("SELECT * FROM leagues")
    List<League> getAllLeagues();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<League> leagues);
}
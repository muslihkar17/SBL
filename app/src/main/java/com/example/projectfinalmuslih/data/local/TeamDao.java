package com.example.projectfinalmuslih.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.projectfinalmuslih.data.model.Team;
import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM teams WHERE idLeague = :leagueId")
    List<Team> getTeamsByLeague(String leagueId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Team> teams);

    // TAMBAHKAN METODE DI BAWAH INI
    @Query("DELETE FROM teams")
    void deleteAll();
}
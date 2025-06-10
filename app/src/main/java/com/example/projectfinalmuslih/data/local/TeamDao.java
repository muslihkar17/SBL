// app/src/main/java/com/example/projectfinalmuslih/data/local/TeamDao.java
package com.example.projectfinalmuslih.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.lifecycle.LiveData; // Import LiveData
import com.example.projectfinalmuslih.data.model.Team;
import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM teams WHERE idLeague = :leagueId")
    List<Team> getTeamsByLeague(String leagueId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Team> teams);

    @Query("DELETE FROM teams WHERE idLeague = :leagueId")
    void deleteByLeague(String leagueId);

    // New: Insert a single team (for favorites)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeam(Team team);

    // New: Delete a single team (for favorites)
    @Query("DELETE FROM teams WHERE idTeam = :teamId")
    void deleteTeam(int teamId);

    // New: Get all favorite teams
    @Query("SELECT * FROM teams")
    LiveData<List<Team>> getAllFavoriteTeams(); // Using LiveData for observing changes

    // New: Check if a team is favorited
    @Query("SELECT EXISTS(SELECT 1 FROM teams WHERE idTeam = :teamId LIMIT 1)")
    boolean isTeamFavorite(int teamId);
}
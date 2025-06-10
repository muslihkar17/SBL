package com.example.projectfinalmuslih.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.projectfinalmuslih.data.model.Player;
import java.util.List;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM players WHERE idTeam = :teamId")
    List<Player> getPlayersByTeam(int teamId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Player> players);

    @Query("DELETE FROM players WHERE idTeam = :teamId")
    void deletePlayersByTeam(int teamId);

    @Transaction
    public default void syncPlayersForTeam(int teamId, List<Player> players) {
        deletePlayersByTeam(teamId);
        if (players != null && !players.isEmpty()) {
            insertAll(players);
        }
    }
}
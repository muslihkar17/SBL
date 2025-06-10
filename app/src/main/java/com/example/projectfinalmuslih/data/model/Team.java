// app/src/main/java/com/example/projectfinalmuslih/data/model/Team.java
package com.example.projectfinalmuslih.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull; // Import for @NonNull

@Entity(tableName = "teams") // Define as a Room entity
public class Team {
    @PrimaryKey
    @NonNull // Ensure primary key is not null
    @SerializedName("idTeam")
    public int idTeam;

    @SerializedName("strTeam")
    public String strTeam;

    @SerializedName("strBadge")
    public String strTeamBadge;

    @SerializedName("strDescriptionEN")
    public String strDescriptionEN;

    @SerializedName("idLeague")
    public String idLeague; // This will help in filtering by league, if needed in favorites
}
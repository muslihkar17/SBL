package com.example.projectfinalmuslih.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "teams")
public class Team {
    @PrimaryKey
    @SerializedName("idTeam")
    public int idTeam;

    @SerializedName("strTeam")
    public String strTeam;

    @SerializedName("strTeamBadge")
    public String strTeamBadge;

    @SerializedName("idLeague")
    public int idLeague;
}
package com.example.projectfinalmuslih.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "players")
public class Player {
    @PrimaryKey
    @SerializedName("idPlayer")
    public int idPlayer;

    @SerializedName("strPlayer")
    public String strPlayer;

    @SerializedName("strTeam")
    public String strTeam;

    @SerializedName("strPosition")
    public String strPosition;

    @SerializedName("strCutout")
    public String strCutout;

    public int idTeam;
}
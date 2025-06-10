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

    @SerializedName("strPosition")
    public String strPosition;

    @SerializedName("strCutout")
    public String strCutout;

    // Foreign key untuk relasi
    public int idTeam;
}
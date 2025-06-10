package com.example.projectfinalmuslih.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "leagues")
public class League {

    @PrimaryKey
    @SerializedName("idLeague")
    public int idLeague;

    @SerializedName("strLeague")
    public String strLeague;

    @SerializedName("strBadge")
    public String strBadge; // URL untuk lencana/badge kecil

    @SerializedName("strLeagueAlternate")
    public String strLeagueAlternate;

    @SerializedName("strSport")
    public String strSport;

    @SerializedName("intFormedYear")
    public String intFormedYear;

    @SerializedName("strCountry")
    public String strCountry;

    @SerializedName("strDescriptionEN")
    public String strDescriptionEN;

    @SerializedName("strWebsite")
    public String strWebsite;

    @SerializedName("strLogo")
    public String strLogo;

    @SerializedName("strFanart1")
    public String strFanart1;

    @SerializedName("strPoster")
    public String strPoster;
}
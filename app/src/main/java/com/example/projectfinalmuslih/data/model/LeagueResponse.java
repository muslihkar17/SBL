package com.example.projectfinalmuslih.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LeagueResponse {

    @SerializedName("leagues")
    private List<League> leagues;

    public List<League> getLeagues() {
        return leagues;
    }
}
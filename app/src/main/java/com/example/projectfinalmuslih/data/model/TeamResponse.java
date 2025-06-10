package com.example.projectfinalmuslih.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TeamResponse {
    // Gunakan @SerializedName untuk mencocokkan "teams" dari JSON
    @SerializedName("teams")
    private List<com.example.projectfinalmuslih.Team> teams;

    // Ganti nama metode agar sesuai dan jelas
    public List<com.example.projectfinalmuslih.Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
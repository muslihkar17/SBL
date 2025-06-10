// PASTIKAN BARIS INI BENAR
package com.example.projectfinalmuslih.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LeagueResponse {

    // Sesuaikan nama key dengan respons API Anda ("leagues" atau "countries")
    @SerializedName("leagues")
    private List<League> leagues;

    public List<League> getLeagues() {
        return leagues;
    }
}
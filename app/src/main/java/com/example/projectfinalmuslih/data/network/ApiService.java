package com.example.projectfinalmuslih.data.network;

import com.example.projectfinalmuslih.data.model.LeagueResponse;
import com.example.projectfinalmuslih.data.model.TeamResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("all_leagues.php")
    Call<LeagueResponse> getLeagues();

    // PERUBAHAN DI SINI: Menggunakan endpoint search_all_teams.php dengan parameter 'l' (nama liga)
    @GET("search_all_teams.php")
    Call<TeamResponse> getTeams(@Query("l") String leagueName);
}
package com.example.projectfinalmuslih;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // Tidak perlu API key
    @GET("search_all_leagues.php?s=Soccer")
    Call<LeagueResponse> getLeagues();

    @GET("search_all_teams.php")
    Call<TeamResponse> getTeams(@Query("l") String leagueName); // pakai nama liga, bukan ID
}

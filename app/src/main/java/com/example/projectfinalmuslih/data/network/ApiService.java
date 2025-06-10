package com.example.projectfinalmuslih.data.network;

import com.example.projectfinalmuslih.data.model.LeagueResponse;
import com.example.projectfinalmuslih.data.model.PlayerResponse;
import com.example.projectfinalmuslih.data.model.TeamResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("all_leagues.php")
    Call<LeagueResponse> getLeagues();

    @GET("lookup_all_teams.php")
    Call<TeamResponse> getTeams(@Query("id") int leagueId);

    // Tambahkan endpoint ini
    @GET("lookup_all_players.php")
    Call<PlayerResponse> getPlayers(@Query("id") int teamId);
}
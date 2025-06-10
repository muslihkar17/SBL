// UBAH BARIS INI: Pindahkan ke dalam paket "data.model"
package com.example.projectfinalmuslih.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlayerResponse {

    // Anotasi ini sudah benar, mencocokkan key "player" dari API
    @SerializedName("player")
    private List<Player> players;

    // Getter ini juga sudah benar
    public List<Player> getPlayers() {
        return players;
    }
}
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

    // --- Field Relevan yang Ditambahkan ---

    /**
     * Nama alternatif untuk liga.
     */
    @SerializedName("strLeagueAlternate")
    public String strLeagueAlternate;

    /**
     * Jenis olahraga, misalnya "Soccer".
     */
    @SerializedName("strSport")
    public String strSport;

    /**
     * Tahun liga pertama kali dibentuk.
     */
    @SerializedName("intFormedYear")
    public String intFormedYear;

    /**
     * Negara asal liga.
     */
    @SerializedName("strCountry")
    public String strCountry;

    /**
     * Deskripsi singkat mengenai liga dalam bahasa Inggris.
     * Sangat berguna untuk halaman detail.
     */
    @SerializedName("strDescriptionEN")
    public String strDescriptionEN;

    /**
     * URL ke situs web resmi liga.
     */
    @SerializedName("strWebsite")
    public String strWebsite;

    /**
     * URL untuk gambar logo. Seringkali kualitasnya lebih baik dari strBadge.
     */
    @SerializedName("strLogo")
    public String strLogo;

    /**
     * URL untuk gambar fan art atau banner.
     * Bagus untuk digunakan sebagai header di halaman detail.
     */
    @SerializedName("strFanart1")
    public String strFanart1;

    /**
     * URL untuk poster liga.
     */
    @SerializedName("strPoster")
    public String strPoster;
}
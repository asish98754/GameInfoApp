package fr.epita.gameinfoapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,
    @SerializedName("picture") val imageUrl: String,
    @SerializedName("type") val type: String? = null,
    @SerializedName("players") val nbPlayers: Int? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("description_en") val description: String? = null,
    @SerializedName("url") val infoUrl: String? = null
) : Serializable {
    override fun toString(): String {
        return "Game(id=$id, title='$title', imageUrl='$imageUrl', type=$type, nbPlayers=$nbPlayers, year=$year, description=$description, infoUrl=$infoUrl)"
    }
}
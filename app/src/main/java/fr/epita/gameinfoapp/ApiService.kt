package fr.epita.gameinfoapp

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // This fetches the list of games from https://www.surleweb.xyz/api/game/list.json
    @GET("game/list.json")
    suspend fun getGameList(): List<Game>

    // This fetches details for a specific game, e.g., for gameId=3, it should fetch from https://www.surleweb.xyz/api/game/details3.json
    @GET("game/details{id}.json")
    suspend fun getGameDetails(@Path("id") id: Int): Game
}
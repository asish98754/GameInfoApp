package fr.epita.gameinfoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.gamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        gamesAdapter = GamesAdapter(emptyList()) { game ->
            Log.d("MainActivity", "Clicked game ID: ${game.id}, Title: ${game.title}")
            val intent = Intent(this@MainActivity, GameDetailsActivity::class.java)
            intent.putExtra("GAME_ID", game.id)
            startActivity(intent)
        }
        recyclerView.adapter = gamesAdapter

        loadGames()
    }

    private fun loadGames() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("MainActivity", "Fetching game list from API: https://www.surleweb.xyz/api/game/list.json")
                val games = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getGameList()
                }
                Log.d("MainActivity", "Fetched games: $games")
                if (games.isNotEmpty()) {
                    gamesAdapter = GamesAdapter(games) { game ->
                        Log.d("MainActivity", "Clicked game ID: ${game.id}, Title: ${game.title}")
                        val intent = Intent(this@MainActivity, GameDetailsActivity::class.java)
                        intent.putExtra("GAME_ID", game.id)
                        startActivity(intent)
                    }
                    recyclerView.adapter = gamesAdapter
                } else {
                    Log.w("MainActivity", "Game list is empty")
                    Toast.makeText(this@MainActivity, "No games available", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading games: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Error loading games: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
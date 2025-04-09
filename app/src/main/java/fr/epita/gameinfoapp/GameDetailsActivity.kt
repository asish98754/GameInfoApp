package fr.epita.gameinfoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        val gameId = intent.getIntExtra("GAME_ID", -1)
        Log.d("GameDetailsActivity", "Received game ID: $gameId")
        if (gameId != -1) {
            loadGameDetails(gameId)
        } else {
            Log.e("GameDetailsActivity", "Invalid game ID received")
            Toast.makeText(this, "Invalid Game ID", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun loadGameDetails(gameId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("GameDetailsActivity", "Fetching game details for ID $gameId from API: https://www.surleweb.xyz/api/game/details$gameId.json")
                val game = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getGameDetails(gameId)
                }
                Log.d("GameDetailsActivity", "Fetched game details: $game")

                // Populate the UI with game details
                findViewById<TextView>(R.id.gameDetailTitle)?.text = game.title ?: "Unknown Title"
                findViewById<TextView>(R.id.gameDetailType)?.text = game.type ?: "N/A"
                findViewById<TextView>(R.id.gameDetailPlayers)?.text = game.nbPlayers?.toString() ?: "N/A"
                findViewById<TextView>(R.id.gameDetailYear)?.text = game.year?.toString() ?: "N/A"
                findViewById<TextView>(R.id.gameDetailDescription)?.text = game.description ?: "No description available"

                // Load the image using Glide
                val imageView = findViewById<ImageView>(R.id.gameDetailImage)
                Glide.with(this@GameDetailsActivity)
                    .load(game.imageUrl ?: R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView)

                // Set up the info button
                findViewById<Button>(R.id.infoButton)?.setOnClickListener {
                    val url = game.infoUrl ?: "https://www.google.com/search?q=${Uri.encode(game.title)}"
                    Log.d("GameDetailsActivity", "Opening URL: $url")
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }

            } catch (e: Exception) {
                Log.e("GameDetailsActivity", "Error loading game details for ID $gameId: ${e.message}", e)
                Toast.makeText(this@GameDetailsActivity, "Error loading details: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
package fr.epita.gameinfoapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GamesAdapter(private val games: List<Game>, private val onClick: (Game) -> Unit) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.gameImage)
        val titleText: TextView = itemView.findViewById(R.id.gameTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }



    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.titleText.text = game.title ?: "Unknown Title"
        Glide.with(holder.itemView.context)
            .load(game.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            Log.d("GamesAdapter", "Item clicked: ${game.title}, ID: ${game.id}")
            onClick(game)
        }
    }

    override fun getItemCount() = games.size
}
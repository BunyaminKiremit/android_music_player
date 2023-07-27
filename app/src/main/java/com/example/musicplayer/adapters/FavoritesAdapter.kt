package com.example.musicplayer.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MusicPlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.models.FavoriteMusic

class FavoritesAdapter(private val favoriteItems: List<FavoriteMusic>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem = favoriteItems[position]
        holder.bind(favoriteItem)
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val favoriteItem = favoriteItems[position]
                    val context = itemView.context

                    // MusicPlayerActivity'yi başlatmak için bir Intent oluşturun
                    val intent = Intent(context, MusicPlayerActivity::class.java)
                    intent.putExtra("musicUrl", favoriteItem.description)
                    intent.putExtra("musicTitle", favoriteItem.title)
                    context.startActivity(intent)
                }
            }
        }


        fun bind(favoriteItem: FavoriteMusic) {
            // Favori öğesinin başlığını ilgili görünüme bağlayın
            titleTextView.text = favoriteItem.title

            // Açıklama (description) görünümünü gizleyin
            descriptionTextView.visibility = View.GONE
        }
    }
}
package com.example.musicplayer.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MusicPlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.models.MusicCategory

class MusicAdapter(private val musicCategories: List<MusicCategory>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_item_layout, parent, false)
        return MusicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicCategory = musicCategories[position]
        holder.bind(musicCategory)

        holder.titleTextView.setOnClickListener {
            musicCategory.isExpanded = !musicCategory.isExpanded
            notifyItemChanged(holder.adapterPosition)
        }

        if (musicCategory.isExpanded) {
            holder.itemsTextView.visibility = View.VISIBLE
            val itemsStringBuilder = StringBuilder()
            for (item in musicCategory.items.orEmpty()) {
                itemsStringBuilder.append(item.title).append("\n")
            }
            holder.itemsTextView.text = itemsStringBuilder.toString()
        } else {
            holder.itemsTextView.visibility = View.GONE
            holder.itemsTextView.text = ""
        }

        holder.itemView.setOnClickListener {
            if (musicCategory.isExpanded && musicCategory.items?.isNotEmpty() == true) {
                val url = musicCategory.items[0].url
                val title = musicCategory.items[0].title // Title'ı al
                val context = holder.itemView.context
                val intent = Intent(context, MusicPlayerActivity::class.java).apply {
                    putExtra("musicUrl", url)
                    putExtra("musicTitle", title) // Title'ı intent ile gönder
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return musicCategories.size
    }

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val itemsTextView: TextView = itemView.findViewById(R.id.itemsTextView)

        fun bind(musicCategory: MusicCategory) {
            titleTextView.text = musicCategory.baseTitle

            if (musicCategory.isExpanded) {
                itemsTextView.visibility = View.VISIBLE
            } else {
                itemsTextView.visibility = View.GONE
            }
        }
    }
}
package com.example.playlistmakernewversion.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.domain.model.Track

class TrackAdapter(private val songs: List<Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var onItemClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(songs[position])
        }
    }

    override fun getItemCount() = songs.size
}
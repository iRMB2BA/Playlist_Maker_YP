package com.example.playlistmakernewversion.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.player.domain.models.Track

class HistoryAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackHolder>() {

    var trackListHistory = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(trackListHistory[position])

        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(trackListHistory[position])

        }
    }

    override fun getItemCount(): Int = trackListHistory.size


}
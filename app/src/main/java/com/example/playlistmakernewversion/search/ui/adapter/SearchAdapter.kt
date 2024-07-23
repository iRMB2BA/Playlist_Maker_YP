package com.example.playlistmakernewversion.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.player.domain.models.Track

class SearchAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackHolder>() {


    var trackList = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackList[position]) }

    }

    override fun getItemCount(): Int = trackList.size


}
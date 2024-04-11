package com.example.playlistmakernewversion

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView
    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView

    init {
        trackImage = itemView.findViewById(R.id.trackImageView)
        trackName = itemView.findViewById(R.id.trackNameView)
        artistName = itemView.findViewById(R.id.artistNameView)
        trackTime = itemView.findViewById(R.id.trackTimeView)
    }

    fun bind(model: Track) {

        Glide.with(itemView)
            .load(model.imageURL)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(trackImage)

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }


}
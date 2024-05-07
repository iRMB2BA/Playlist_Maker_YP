package com.example.playlistmakernewversion

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImageView)
    private val trackName: TextView = itemView.findViewById(R.id.trackNameView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameView)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTimeView)

    fun bind(model: Track, listener: TrackAdapter.Listener) {

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(trackImage)

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = dateFormat.format(model.trackTimeMillis)

        itemView.setOnClickListener {
            listener.onClick(model)
        }
    }
}
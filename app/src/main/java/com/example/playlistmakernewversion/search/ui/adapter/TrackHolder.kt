package com.example.playlistmakernewversion.search.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.databinding.TrackCardViewBinding
import com.example.playlistmakernewversion.player.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = TrackCardViewBinding.bind(item)
    fun bind(track: Track) = with(binding) {
        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.corners_image_track)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(trackImageView)

        trackNameView.text = track.artistName
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis?.toInt())
        trackNameView.text = track.trackName

    }

}
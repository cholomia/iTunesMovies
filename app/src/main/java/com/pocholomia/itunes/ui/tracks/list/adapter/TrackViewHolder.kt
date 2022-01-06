package com.pocholomia.itunes.ui.tracks.list.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocholomia.itunes.R
import com.pocholomia.itunes.databinding.ItemTrackBinding
import com.pocholomia.itunes.domain.model.Track

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    private val onClick: (Track) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track?) {
        binding.root.setOnClickListener {
            track?.let { onClick(it) }
        }
        itemView.isVisible = track != null
        track?.let { showData(it) }
    }

    private fun showData(track: Track) {
        Glide.with(itemView)
            .load(track.getSmallArtworkUrl)
            .fitCenter()
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
            .into(binding.imgPoster)
        binding.txtTitle.text = track.name
        binding.txtPrice.text = track.priceDisplay
        binding.txtGenre.text = track.primaryGenreName
    }

}
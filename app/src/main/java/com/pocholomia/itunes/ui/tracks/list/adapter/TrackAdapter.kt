package com.pocholomia.itunes.ui.tracks.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pocholomia.itunes.R
import com.pocholomia.itunes.databinding.ItemHeaderBinding
import com.pocholomia.itunes.databinding.ItemTrackBinding
import com.pocholomia.itunes.domain.model.Track
import com.pocholomia.itunes.ui.tracks.list.TrackItem

class TrackAdapter(
    private val onClick: (Track) -> Unit
) : PagingDataAdapter<TrackItem, RecyclerView.ViewHolder>(TrackDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TrackItem.Header -> R.layout.item_header
            is TrackItem.Item -> R.layout.item_track
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_track) {
            TrackViewHolder(
                ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClick
            )
        } else {
            HeaderViewHolder(
                ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { trackItem ->
            when (trackItem) {
                is TrackItem.Header -> (holder as HeaderViewHolder).bind(trackItem.previousVisit)
                is TrackItem.Item -> (holder as TrackViewHolder).bind(trackItem.track)
            }
        }
    }

    object TrackDiffCallback : DiffUtil.ItemCallback<TrackItem>() {

        override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return (oldItem is TrackItem.Item && newItem is TrackItem.Item && oldItem.track.id == newItem.track.id)
                    || (oldItem is TrackItem.Header && newItem is TrackItem.Header && oldItem.previousVisit == newItem.previousVisit)
        }

        override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem == newItem
        }

    }

}
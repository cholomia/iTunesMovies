package com.pocholomia.itunes.ui.tracks.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.pocholomia.itunes.R
import com.pocholomia.itunes.databinding.ItemHeaderBinding
import com.pocholomia.itunes.ui.utils.toReadableString
import java.util.*

class HeaderViewHolder(
    private val binding: ItemHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(previousVisit: Date?) {
        binding.txtHeader.text = if (previousVisit != null) {
            itemView.context.getString(
                R.string.txt_previous_visit,
                previousVisit.toReadableString("MMMM dd, yyyy hh:mm aa")
            )
        } else {
            itemView.context.getString(R.string.msg_first_visit)
        }
    }

}
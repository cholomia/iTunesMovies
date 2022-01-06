package com.pocholomia.itunes.ui.tracks.list

import com.pocholomia.itunes.domain.model.Track
import java.util.*

sealed class TrackItem {

    data class Item(val track: Track) : TrackItem()

    data class Header(val previousVisit: Date?) : TrackItem()

}
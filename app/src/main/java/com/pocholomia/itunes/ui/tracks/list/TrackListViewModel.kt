package com.pocholomia.itunes.ui.tracks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pocholomia.itunes.domain.model.Track
import com.pocholomia.itunes.domain.usecase.TrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    trackUseCase: TrackUseCase
) : ViewModel() {

    val tracksPagingDataFlow: Flow<PagingData<TrackItem>>

    init {
        val previousVisit = trackUseCase.getPreviousVisit()
        val previousVisitDate = if (previousVisit > 0L) Date(previousVisit) else null
        trackUseCase.setNewVisit(Date().time)

        tracksPagingDataFlow = trackUseCase.getTracks()
            .map { data -> data.map<Track, TrackItem> { TrackItem.Item(it) } }
            .map {
                // insert header
                it.insertHeaderItem(
                    TerminalSeparatorType.SOURCE_COMPLETE,
                    TrackItem.Header(previousVisitDate)
                )
            }
            .cachedIn(viewModelScope)
    }

}
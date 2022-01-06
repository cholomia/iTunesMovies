package com.pocholomia.itunes.ui.tracks.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocholomia.itunes.domain.model.Track
import com.pocholomia.itunes.domain.usecase.TrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * `private val _` variables are use for encapsulation so that only the ViewModel can update on the MutableLiveData, only expose LiveData for views
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TrackDetailViewModel @Inject constructor(
    private val trackUseCase: TrackUseCase
) : ViewModel() {

    private val trackIdStateFlow = MutableSharedFlow<Long>()

    private val _error = MutableSharedFlow<Throwable>()
    val error: SharedFlow<Throwable> = _error

    // uses LiveData for simple observe with the view lifecycle
    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    init {
        viewModelScope.launch {
            trackIdStateFlow.distinctUntilChanged()
                .flatMapLatest { trackUseCase.getTrackDetail(it) }
                .catch {
                    _error.emit(it)
                }
                .collectLatest {
                    _track.postValue(it)
                }
        }
    }

    fun getTrack(trackId: Long) {
        viewModelScope.launch {
            trackIdStateFlow.emit(trackId)
        }
    }

}
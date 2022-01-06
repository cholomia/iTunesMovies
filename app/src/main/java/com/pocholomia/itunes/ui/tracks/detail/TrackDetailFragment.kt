package com.pocholomia.itunes.ui.tracks.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.pocholomia.itunes.R
import com.pocholomia.itunes.databinding.FragmentTrackDetailBinding
import com.pocholomia.itunes.domain.model.Track
import com.pocholomia.itunes.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackDetailFragment : Fragment(R.layout.fragment_track_detail) {

    private val binding by viewBinding(FragmentTrackDetailBinding::bind)
    private val viewModel by viewModels<TrackDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val trackId = TrackDetailFragmentArgs.fromBundle(requireArguments()).trackId
        viewModel.getTrack(trackId)

        viewModel.track.observe(viewLifecycleOwner, ::setTrack)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest {
                Snackbar.make(
                    binding.root,
                    it.localizedMessage ?: getString(R.string.msg_unknown_error),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setTrack(track: Track) {
        Glide.with(this)
            .load(track.getBigArtworkUrl)
            .into(binding.imgPoster)
        binding.txtTitle.text = track.name
        binding.txtPrice.text = track.priceDisplay
        binding.txtGenre.text = track.primaryGenreName
        binding.txtReleaseDate.text = track.releaseDateDisplay
        binding.txtDescription.text = track.descriptionDisplay
        binding.txtTime.text = track.runningTime
    }

}
package com.pocholomia.itunes.ui.tracks.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocholomia.itunes.R
import com.pocholomia.itunes.databinding.FragmentTrackListBinding
import com.pocholomia.itunes.ui.tracks.list.adapter.BasicLoadStateAdapter
import com.pocholomia.itunes.ui.tracks.list.adapter.TrackAdapter
import com.pocholomia.itunes.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackListFragment : Fragment(R.layout.fragment_track_list) {

    private val binding by viewBinding(FragmentTrackListBinding::bind)
    private val viewModel by viewModels<TrackListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TrackAdapter {
            findNavController().navigate(TrackListFragmentDirections.openDetail(it.id))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val footer = BasicLoadStateAdapter { adapter.retry() }
        binding.recyclerView.adapter = adapter.withLoadStateFooter(footer)
        binding.swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }
        binding.btnRetry.setOnClickListener { adapter.retry() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tracksPagingDataFlow.collectLatest(adapter::submitData)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                binding.swipeRefreshLayout.isRefreshing =
                    loadState.mediator?.refresh is LoadState.Loading
                val itemCount = adapter.snapshot().filterIsInstance<TrackItem.Item>().count()
                val isEmpty = loadState.refresh is LoadState.NotLoading && itemCount == 0
                val isError =
                    loadState.mediator?.refresh is LoadState.Error && itemCount == 0

                binding.layoutEmptyState.isVisible = isEmpty || isError
                binding.btnRetry.isVisible = isError
                binding.errorMsg.text = if (isError) {
                    (loadState.refresh as? LoadState.Error)?.error?.localizedMessage
                        ?: getString(R.string.msg_unknown_error)
                } else {
                    getString(R.string.msg_empty_list)
                }
            }
        }
    }

}
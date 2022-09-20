package com.example.mazechallenge.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mazechallenge.R
import com.example.mazechallenge.common.Status
import com.example.mazechallenge.common.collectLifecycleFlow
import com.example.mazechallenge.common.onError
import com.example.mazechallenge.common.onLoading
import com.example.mazechallenge.databinding.FragmentEpisodeListBinding
import com.example.mazechallenge.ui.adapter.EpisodeListAdapter
import com.example.mazechallenge.ui.viewmodel.EpisodeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeListFragment : Fragment(R.layout.fragment_episode_list) {
    private lateinit var binding: FragmentEpisodeListBinding
    private lateinit var episodeListViewModel: EpisodeListViewModel

    @Inject
    lateinit var episodeListAdapter: EpisodeListAdapter

    private val args: EpisodeListFragmentArgs by navArgs()

    private var lastPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodeListBinding.bind(view)
        episodeListViewModel =
            ViewModelProvider(requireActivity())[EpisodeListViewModel::class.java]

        binding.elvEpisodes.apply {
            setAdapter(episodeListAdapter)
        }

        binding.elvEpisodes.setOnGroupExpandListener { groupPosition ->
            if (lastPosition != -1 && groupPosition != lastPosition) {
                //binding.elvEpisodes.collapseGroup(lastPosition)
            }
            lastPosition = groupPosition
        }

        collectLifecycleFlow(episodeListViewModel.episodeList) {
            onLoading(it.status == Status.LOADING)
            when (it.status) {
                Status.ERROR -> onError(it.message)
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        episodeListAdapter.map = list
                        episodeListAdapter.notifyDataSetChanged()
                        binding.elvEpisodes.expandGroup(0)
                    }
                }
                else -> {}
            }
        }

        episodeListViewModel.fetchShowEpisodes(args.show)
    }

}
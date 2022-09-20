package com.example.mazechallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazechallenge.common.Resource
import com.example.mazechallenge.model.Episode
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeListViewModel @Inject constructor(
    private val episodeRepo: EpisodeRepository
) : ViewModel() {

    private val _episodeList = MutableStateFlow<Resource<Map<Int, List<Episode>>>>(Resource.loading(null))
    val episodeList = _episodeList.asStateFlow()

    fun fetchShowEpisodes(show: Show) {
        viewModelScope.launch {
            if (show.episodes != null) {
                _episodeList.emit(Resource.success(formatSuccessfulList(show.episodes!!)))
                return@launch
            }
            _episodeList.emit(Resource.loading(null))
            try {
                episodeRepo.fetchShowEpisodes(show)
                val listFromRepo = show.episodes
                if (listFromRepo == null) {
                    _episodeList.emit(Resource.error("Episodes not stored in TVMaze", null))
                } else {
                    _episodeList.emit(Resource.success(formatSuccessfulList(listFromRepo)))
                }
            } catch (e: Exception) {
                _episodeList.emit(Resource.error("An unknown error has occurred", e))
            }
        }
    }

    private fun formatSuccessfulList(list: List<Episode>) = list.groupBy { it.season }
}
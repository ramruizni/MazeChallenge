package com.example.mazechallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazechallenge.common.Resource
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val showRepo: ShowRepository
) : ViewModel() {

    private val _favoriteList = MutableStateFlow<Resource<List<Show>>>(Resource.loading(null))
    val favoriteList = _favoriteList.asStateFlow()

    private val _favoriteToggle = Channel<Boolean>()
    val favoriteToggle = _favoriteToggle.receiveAsFlow()

    fun fetchFavoriteList() {
        viewModelScope.launch {
            _favoriteList.emit(Resource.loading(null))
            try {
                val listOfFavorites = showRepo.fetchFavoriteList()
                _favoriteList.emit(Resource.success(listOfFavorites))
            } catch (e: Exception) {
                _favoriteList.emit(Resource.error("An unknown error has occurred", e))
            }
        }
    }

    fun toggleShowAsFavorite(show: Show) {
        viewModelScope.launch {
            showRepo.toggleShowAsFavorite(show)
            _favoriteToggle.send(show.isFavorite)
        }
    }
}
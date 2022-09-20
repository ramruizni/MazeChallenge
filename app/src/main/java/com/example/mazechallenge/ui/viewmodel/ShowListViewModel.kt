package com.example.mazechallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazechallenge.common.Resource
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ShowListViewModel @Inject constructor(
    private val showRepo: ShowRepository
) : ViewModel() {

    private val _showList = MutableStateFlow<Resource<List<Show>>>(Resource.loading(null))
    val showList = _showList.asStateFlow()
    private var _showListCached: List<Show> = mutableListOf()

    fun fetchListByName(name: String) {
        viewModelScope.launch {
            _showList.emit(Resource.loading(null))
            try {
                _showList.emit(Resource.success(showRepo.fetchListByName(name)))
            } catch (e: Exception) {
                _showList.emit(Resource.error("An unknown error has occurred", e))
            }
        }
    }

    fun fetchFirstPageIfEmpty() {
        viewModelScope.launch {
            try {
                val listFromRepo = showRepo.getList()
                _showListCached = listFromRepo.ifEmpty {
                    _showList.emit(Resource.loading(null))
                    showRepo.fetchListByPage(0)
                }
                _showList.emit(Resource.success(_showListCached))
            } catch (e: Exception) {
                _showList.emit(Resource.error("An unknown error has occurred", e))
            }
        }
    }

    fun fetchNextPage() {
        viewModelScope.launch {
            _showList.emit(Resource.loading(null))
            try {
                val maxId = _showListCached[_showListCached.size - 1].id
                val nextPage = ceil(maxId / 250.0).toInt()
                _showListCached = _showListCached + showRepo.fetchListByPage(nextPage)
                _showList.emit(Resource.success(_showListCached))
            } catch (e: Exception) {
                _showList.emit(Resource.error("An unknown error has occurred", e))
            }
        }
    }
}
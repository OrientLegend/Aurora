package com.eternal.aurora.ui.page.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.PhotoCollection
import com.eternal.aurora.logic.model.SearchUserInfo
import com.eternal.aurora.logic.model.UserInfo
import com.eternal.aurora.logic.network.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {

    private val _searchPhotosState = MutableStateFlow(emptyList<Photo>())
    val searchPhotosState = _searchPhotosState.asStateFlow()

    private val _searchCollectionsState = MutableStateFlow(emptyList<PhotoCollection>())
    val searchCollectionsState = _searchCollectionsState.asStateFlow()

    private val _searchUsersState = MutableStateFlow(emptyList<SearchUserInfo>())
    val searchUsersState = _searchUsersState.asStateFlow()

    val isSearching = mutableStateOf(false)

    val searchString = mutableStateOf("")

    fun searchPhotos(searchString: String) {
        viewModelScope.launch {
            val result = Network.getSearchPhotosResult(queryString = searchString).getOrNull()
            if (result != null) {
                _searchPhotosState.value = result
                isSearching.value = false
            }
        }
    }

    fun searchCollections(searchString: String) {
        viewModelScope.launch {
            val result = Network.getSearchCollectionsResult(queryString = searchString).getOrNull()
            if (result != null) {
                _searchCollectionsState.value = result
            }
        }
    }

    fun searchUsers(searchString: String) {
        viewModelScope.launch {
            val result = Network.getSearchUsersResult(queryString = searchString).getOrNull()
            if (result != null) {
                _searchUsersState.value = result
            }
        }
    }

}
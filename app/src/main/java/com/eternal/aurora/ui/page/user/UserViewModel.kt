package com.eternal.aurora.ui.page.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.database.entity.FollowingUsername
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.PhotoCollection
import com.eternal.aurora.logic.model.UserDetail
import com.eternal.aurora.logic.network.Network
import com.eternal.aurora.ui.utils.DatabaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(): ViewModel(){

    private val _userDetailState: MutableStateFlow<UserDetail?> = MutableStateFlow(null)
    val userDetailState = _userDetailState.asStateFlow()

    private val _userPhotosState = MutableStateFlow(emptyList<Photo>())
    val userPhotosState = _userPhotosState.asStateFlow()

    private val _userCollectionsState = MutableStateFlow(emptyList<PhotoCollection>())
    val userCollectionsState = _userCollectionsState.asStateFlow()

    private val _userLikesState = MutableStateFlow(emptyList<Photo>())
    val userLikesState = _userLikesState.asStateFlow()

    private val _isFollowing = MutableStateFlow(flowOf(false))
    val isFollowing = _isFollowing.asStateFlow()

    var selectedIndex = mutableStateOf(0)

    var currentPhotoPage = 1
    private var previousPhotoPage = 0

    fun getUserDetail(username: String) {
        if (currentPhotoPage > previousPhotoPage){
            viewModelScope.launch {
                val result = Network.getUserDetail(username).getOrNull()
                _isFollowing.value = DatabaseUtil.loadAllFollowingUsername().map {
                    it.contains(FollowingUsername(username))
                }
                if (result != null) {
                    _userDetailState.value = result
                }
            }
            previousPhotoPage = currentPhotoPage
        }
    }

    fun getUserPhotos(username: String, page: Int = 1) {
        viewModelScope.launch {
            val result = Network.getUserPhotos(username, page).getOrNull()
            if (result != null) {
                _userPhotosState.value = result
            }
        }
    }

    fun getUserCollections(username: String, page: Int = 1) {
        viewModelScope.launch {
            val result = Network.getUserCollections(username, page).getOrNull()
            if (result != null) {
                _userCollectionsState.value = result
            }
        }
    }

    fun getUserLikes(username: String, page: Int = 1) {
        viewModelScope.launch {
            val result = Network.getUserLikes(username, page).getOrNull()
            if (result != null) {
                _userLikesState.value = result
            }
        }
    }

    fun addFollowingUser(userDetail: UserDetail) {
        viewModelScope.launch {
            DatabaseUtil.insertOrUpdateFollowingUser(userDetail)
        }
    }

    fun removeFollowingUser(userDetail: UserDetail) {
        viewModelScope.launch {
            DatabaseUtil.deleteFollowingUser(userDetail.username)
        }
    }


}
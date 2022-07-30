package com.eternal.aurora.ui.page.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.database.entity.FavoritePhotoId
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.PhotoDetail
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
class PhotoViewModel @Inject constructor(): ViewModel(){

    private val _photoDetailState = MutableStateFlow<PhotoDetail?>(null)
    val photoDetailState = _photoDetailState.asStateFlow()

    private val _isFavorite = MutableStateFlow(flowOf(false))
    val isFavorite = _isFavorite.asStateFlow()


    fun getPhotoDetail(id: String) {
        viewModelScope.launch {
            val response = Network.getPhotoDetail(id).getOrNull()
            _isFavorite.value = DatabaseUtil.loadAllFavoritePhotosId().map {
                it.contains(FavoritePhotoId(id))
            }
            if(response != null) {
                _photoDetailState.value = response
            }
        }
    }

    fun addFavoritePhoto(photo: Photo) {
        viewModelScope.launch {
            DatabaseUtil.insertOrUpdateFavoritePhoto(photo)
        }
    }

    fun removeFavoritePhoto(photo: Photo) {
        viewModelScope.launch {
            DatabaseUtil.deleteFavoritePhotoById(photo.id)
        }
    }

}
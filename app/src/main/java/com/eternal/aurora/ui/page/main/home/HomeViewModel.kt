package com.eternal.aurora.ui.page.main.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.network.Network
import com.eternal.aurora.ui.utils.DatabaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val _photosState = MutableStateFlow(flowOf(emptyList<Photo>()))
    val photosState = _photosState.asStateFlow()

    val showAlert = mutableStateOf(false)


    init {
        tryToGetPhotos()
        loadPhotosFromDb()
    }

    fun tryToGetPhotos() {
        viewModelScope.launch {
            val result = Network.getPhotos()
            val response = result.getOrNull()
            if(response != null) {
                response.reversed().forEach {
                    DatabaseUtil.insertOrUpdateHomePhoto(it) //Reverse insertion
                }
            } else {
                showAlert.value = true
            }
        }
    }

    fun loadPhotosFromDb() {
        _photosState.value = DatabaseUtil.loadAllHomePhoto()
    }

    fun deletePhotoById(id: String) {
        viewModelScope.launch {
            DatabaseUtil.deleteHomePhotoById(id)
        }
    }
}
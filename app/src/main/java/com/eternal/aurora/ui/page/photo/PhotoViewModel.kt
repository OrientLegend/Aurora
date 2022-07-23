package com.eternal.aurora.ui.page.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.PhotoDetail
import com.eternal.aurora.logic.network.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoViewModel @Inject constructor(): ViewModel(){

    private val _photoDetailState = MutableStateFlow<PhotoDetail?>(null)
    val photoDetailState = _photoDetailState.asStateFlow()

    fun getPhotoDetail(id: String) {
        viewModelScope.launch {
            val response = Network.getPhotoDetail(id).getOrNull()
            if(response != null) {
                _photoDetailState.value = response
            }
        }
    }

}
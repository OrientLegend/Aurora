package com.eternal.aurora.ui.page.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.network.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val _photoState = MutableStateFlow(emptyList<Photo>())
    val photoState = _photoState.asStateFlow()

    init {
        viewModelScope.launch {
            val response = Network.getPhotos().getOrNull()
            if(response != null) {
                _photoState.value = response
            }
        }
    }


}
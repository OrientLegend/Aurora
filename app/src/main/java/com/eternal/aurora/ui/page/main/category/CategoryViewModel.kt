package com.eternal.aurora.ui.page.main.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.PhotoCollection
import com.eternal.aurora.logic.network.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(): ViewModel(){

    private val _photoCollectionsState = MutableStateFlow(emptyList<PhotoCollection>())
    val photoCollectionsState = _photoCollectionsState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = Network.getPhotoCollections().getOrNull()
            if(result != null) {
                _photoCollectionsState.value = result
            }
        }
    }

}
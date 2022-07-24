package com.eternal.aurora.ui.page.collection

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
class CollectionViewModel @Inject constructor(): ViewModel() {

    private val _photoListState = MutableStateFlow(emptyList<Photo>())
    val photoListState = _photoListState.asStateFlow()

    fun getPhotoList(collectionId: String) {
        viewModelScope.launch {
            val result = Network.getPhotosFromCollection(collectionId).getOrNull()
            if(result != null) {
                _photoListState.value = result
            }
        }
    }

}
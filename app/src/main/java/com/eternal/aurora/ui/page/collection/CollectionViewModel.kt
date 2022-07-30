package com.eternal.aurora.ui.page.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.network.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CollectionViewModel @Inject constructor() : ViewModel() {

    private val _photosSizeState = MutableStateFlow(0)
    val photosSizeState = _photosSizeState.asStateFlow()

    private val _photoListState = MutableStateFlow(mutableListOf<Photo>())
    val photoListState: StateFlow<List<Photo>> get() = _photoListState.asStateFlow()

    var currentPage by mutableStateOf(1)

    private var previousPage = 0
    fun getPhotoListFromCollection(collectionId: String) {
        if (currentPage > previousPage){
            viewModelScope.launch {
                val result =
                    Network.getPhotosFromCollection(collectionId, page = currentPage).getOrNull()
                if (result != null) {
                    _photoListState.value.addAll(result)
                }
                _photosSizeState.value = _photoListState.value.size
            }
            previousPage = currentPage
        }
    }


}
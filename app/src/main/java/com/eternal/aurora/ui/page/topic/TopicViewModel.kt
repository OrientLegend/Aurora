package com.eternal.aurora.ui.page.topic

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
class TopicViewModel @Inject constructor(): ViewModel(){

    private val _photosSizeState = MutableStateFlow(0)
    val photosSizeState = _photosSizeState.asStateFlow()

    private val _photoListState = MutableStateFlow(mutableListOf<Photo>())
    val photoListState: StateFlow<List<Photo>> get() = _photoListState.asStateFlow()

    private var currentPage = 0

    fun getPhotoListFromTopic(topicId: String, page: Int) {
        if (page > currentPage) { //To avoid get the same data
            viewModelScope.launch {
                val result = Network.getPhotosFromTopic(topicId = topicId, page = page).getOrNull()
                if (result != null) {
                    _photoListState.value.addAll(result)
                    _photosSizeState.value = _photoListState.value.size
                }
            }
            currentPage = page
        }
    }
}
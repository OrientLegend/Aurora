package com.eternal.aurora.ui.page.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.ui.utils.DatabaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(): ViewModel(){

    private val _favoritePhotosState = MutableStateFlow(flowOf(emptyList<Photo>()))
    val favoritePhotosState = _favoritePhotosState.asStateFlow()

    init {
        viewModelScope.launch {
            _favoritePhotosState.value = DatabaseUtil.loadAllFavoritePhotos()
        }
    }


}
package com.eternal.aurora.ui.page.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.eternal.aurora.ui.page.photo.PhotoListPage

@Composable
fun HomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onOpenPhoto: (String) -> Unit
) {
    val photoState = homeViewModel.photosState.collectAsState()

    val photoListState = photoState.value.collectAsState(initial = emptyList())


    Column {
        PhotoListPage(photoList = photoListState.value, onOpenPhoto = onOpenPhoto)
    }
}
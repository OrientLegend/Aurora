package com.eternal.aurora.ui.page.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eternal.aurora.ui.page.photo.PhotoListPage

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenPhoto: (String) -> Unit,
    openUser: (String) -> Unit
) {
    val photoState = viewModel.photosState.collectAsState()

    val photoListState = photoState.value.collectAsState(initial = emptyList())


    if (photoListState.value.size > 20) {
        viewModel.deletePhotoById(photoListState.value[0].id)
    }

    Column(modifier = modifier) {
        PhotoListPage(
            photoList = photoListState.value.reversed(), onOpenPhoto = onOpenPhoto,
            count = photoListState.value.size,
            openUser = openUser
        )
    }
}
package com.eternal.aurora.ui.page.collection

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eternal.aurora.ui.page.photo.PhotoListPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPage(
    collectionId: String,
    collectionViewModel: CollectionViewModel = hiltViewModel(),
    onOpenPhoto: (String) -> Unit
) {
    collectionViewModel.getPhotoList(collectionId)

    val photoListState = collectionViewModel.photoListState.collectAsState()

    Scaffold(modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding()
    ) { innerPadding ->
        PhotoListPage(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp),
            photoList = photoListState.value,
            onOpenPhoto = onOpenPhoto
        )
    }
}
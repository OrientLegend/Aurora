package com.eternal.aurora.ui.page.collection

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eternal.aurora.ui.page.photo.PhotoListLayout
import com.eternal.aurora.ui.page.photo.PhotoListPage
import com.eternal.aurora.ui.theme.AuroraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPage(
    collectionId: String,
    collectionName: String,
    totalPhotos: Int,
    collectionViewModel: CollectionViewModel = hiltViewModel(),
    onOpenPhoto: (String) -> Unit,
    upPress: () -> Unit,
) {

    collectionViewModel.getPhotoListFromCollection(collectionId)

    val photoListState = collectionViewModel.photoListState.collectAsState()
    val photosSizeState = collectionViewModel.photosSizeState.collectAsState()

    AuroraTheme {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    IconButton(onClick = { upPress() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }

                    Text(
                        text = collectionName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        ) { innerPadding ->
            PhotoListPage(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                photoList = photoListState.value,
                count = photosSizeState.value,
                onOpenPhoto = onOpenPhoto,
                photoListLayout = PhotoListLayout.IsGrid,
                loadMore = {
                    collectionViewModel.currentPage++
                    collectionViewModel.getPhotoListFromCollection(collectionId)
                }
            )
        }
    }
}
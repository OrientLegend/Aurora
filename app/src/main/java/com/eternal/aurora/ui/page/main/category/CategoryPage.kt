package com.eternal.aurora.ui.page.main.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eternal.aurora.logic.model.PhotoCollection
import com.eternal.aurora.R

@Composable
fun CategoryPage(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onOpenCollection: (String) -> Unit
) {

    val photoCollectionsState = categoryViewModel.photoCollectionsState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(count = photoCollectionsState.value.size) { index ->
            CollectionCard(
                photoCollection = photoCollectionsState.value[index],
                modifier = Modifier.padding(bottom = 20.dp),
                onOpenCollection = onOpenCollection
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    photoCollection: PhotoCollection,
    onOpenCollection: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .clickable { onOpenCollection(photoCollection.id) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = photoCollection.coverPhoto.urls.regular, contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.lighting(
                    multiply = Color(0xAAAAAAAA), // To highlight the title
                    add = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = 16.dp,
                        bottom = 16.dp
                    )
            ) {
                Text(
                    text = photoCollection.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(
                        id = R.string.photos_number,
                        photoCollection.totalPhotos
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}
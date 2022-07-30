package com.eternal.aurora.ui.page.main.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eternal.aurora.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onOpenCollection: (String, String, Int) -> Unit,
    onOpenTopic: (String, String, Int) -> Unit
) {

    val photoCollectionsState = categoryViewModel.photoCollectionsState.collectAsState()

    val topicsState = categoryViewModel.topicsState.collectAsState()

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),

            ) {
            Tab(selected = 0 == pagerState.currentPage, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }) {
                Text(
                    text = stringResource(id = R.string.category),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = if (pagerState.currentPage != 0) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.primary
                )
            }
            Tab(selected = 1 == pagerState.currentPage, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }) {
                Text(
                    text = stringResource(id = R.string.topic),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = if (pagerState.currentPage != 1) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.primary
                )
            }
        }

        HorizontalPager(
            count = 2, state = pagerState
        ) { page ->
            when (page) {
                0 -> LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(count = photoCollectionsState.value.size) { index ->
                        val collection = photoCollectionsState.value[index]
                        CollectionCard(
                            modifier = Modifier.padding(bottom = 20.dp),
                            onOpenCollection = onOpenCollection,
                            collectionId = collection.id,
                            collectionTitle = collection.title,
                            coverUrl = collection.coverPhoto.urls.regular,
                            totalPhotos = collection.totalPhotos
                        )
                    }
                }
                1 -> LazyColumn(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(count = topicsState.value.size) { index ->
                        val topic = topicsState.value[index]
                        CollectionCard(
                            modifier = Modifier.padding(bottom = 20.dp),
                            onOpenCollection = onOpenTopic,
                            collectionId = topic.id,
                            collectionTitle = topic.title,
                            coverUrl = topic.coverPhoto.urls.regular,
                            totalPhotos = topic.totalPhotos,

                        )
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collectionId: String,
    collectionTitle: String,
    coverUrl: String,
    totalPhotos: Int,
    onOpenCollection: (String, String, Int) -> Unit
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1.5f)
        .clickable { onOpenCollection(collectionId, collectionTitle, totalPhotos) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
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
                        start = 16.dp, bottom = 16.dp
                    )
            ) {
                Text(
                    text = collectionTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(
                        id = R.string.photos_number, totalPhotos
                    ), style = MaterialTheme.typography.bodyMedium, color = Color.White
                )
            }
        }
    }
}
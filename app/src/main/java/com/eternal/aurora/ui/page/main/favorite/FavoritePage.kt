package com.eternal.aurora.ui.page.main.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eternal.aurora.R
import com.eternal.aurora.ui.page.photo.PhotoListLayout
import com.eternal.aurora.ui.page.photo.PhotoListPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FavoritePage(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = hiltViewModel(),
    openPhoto: (String) -> Unit
) {

    val photoList by
    viewModel.favoritePhotosState.collectAsState().value.collectAsState(initial = emptyList())

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf(stringResource(id = R.string.photos), stringResource(id = R.string.following))

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, tab ->
                Tab(selected = index == pagerState.currentPage, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }) {
                    Text(
                        text = tab,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = if (pagerState.currentPage != index) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        HorizontalPager(count = tabs.size, state = pagerState) { page ->
            when (page) {
                0 -> {
                    PhotoListPage(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        photoList = photoList,
                        count = photoList.size,
                        onOpenPhoto = openPhoto,
                        photoListLayout = PhotoListLayout.IsGrid,
                        spacerHeight = 12.dp,
                        showLoadingAnimation = false
                    )
                }
            }
        }
    }
}
package com.eternal.aurora.ui.page.main.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eternal.aurora.R
import com.eternal.aurora.logic.model.UserDetail
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
    openPhoto: (String) -> Unit,
    openUser: (String) -> Unit
) {

    val photoList by
    viewModel.favoritePhotosState.collectAsState().value.collectAsState(initial = emptyList())
    val followingUserList by
    viewModel.followingUserState.collectAsState().value.collectAsState(initial = emptyList())

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
                1 -> {
                    LazyColumn {
                        items(count = followingUserList.size) { index ->
                            val followingUser = followingUserList[index]
                            FollowingUserItem(userDetail = followingUser, openUser = openUser)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FollowingUserItem(
    userDetail: UserDetail,
    modifier: Modifier = Modifier,
    openUser: (String) -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = {
                openUser(userDetail.username)
            }, indication = null, interactionSource = remember{
                MutableInteractionSource()
            })
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        AsyncImage(
            model = userDetail.profileImage.mediumScale,
            contentDescription = userDetail.name,
            modifier = Modifier
                .size(64.dp)
                .clip(
                    RoundedCornerShape(100f)
                )
        )
        Text(
            text = userDetail.name,
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
        )
    }
}
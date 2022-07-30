package com.eternal.aurora.ui.page.user

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eternal.aurora.R
import com.eternal.aurora.logic.model.UserDetail
import com.eternal.aurora.ui.animation.LoadingAnimation
import com.eternal.aurora.ui.page.main.category.CollectionCard
import com.eternal.aurora.ui.page.photo.PhotoComponent
import com.eternal.aurora.ui.theme.AuroraTheme


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun UserPage(
    username: String,
    viewModel: UserViewModel = hiltViewModel(),
    upPress: () -> Unit,
    openPhoto: (String) -> Unit = {},
    openCollection: (String, String, Int) -> Unit
) {
    viewModel.getUserDetail(username)
    viewModel.getUserPhotos(username)
    viewModel.getUserCollections(username)
    viewModel.getUserLikes(username)
    val userDetail by viewModel.userDetailState.collectAsState()
    val userPhotos by viewModel.userPhotosState.collectAsState()
    val userCollections by viewModel.userCollectionsState.collectAsState()
    val userLikes by viewModel.userLikesState.collectAsState()
    val isFollowing by viewModel.isFollowing.collectAsState().value.collectAsState(initial = false)

    AuroraTheme {
        if (userDetail != null) {
            val tabs = listOf(
                TabItem(
                    stringResource(id = R.string.photos),
                    Icons.Outlined.Photo,
                    userDetail?.totalPhotos != 0
                ),
                TabItem(
                    stringResource(id = R.string.collections),
                    Icons.Outlined.Collections,
                    userDetail?.totalCollections != 0
                ),
                TabItem(
                    stringResource(id = R.string.likes),
                    Icons.Outlined.Favorite,
                    userDetail?.totalLikes != 0
                )
            ).filter { it.isShow }
            val photosDescription = stringResource(id = R.string.photos)
            val collectionsDescription = stringResource(id = R.string.collections)
            val likesDescription = stringResource(id = R.string.likes)

            val decayAnimationSpec = rememberSplineBasedDecay<Float>()
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                decayAnimationSpec,
                rememberTopAppBarScrollState()
            )

            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
                    .navigationBarsPadding(),
                topBar = {
                    MediumTopAppBar(
                        title = {
                            Text(
                                text = userDetail?.name ?: "",
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        top = 6.dp,
                                        bottom = 6.dp
                                    )
                                    .statusBarsPadding()
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(vertical = 6.dp),
                                onClick = upPress
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            Button(
                                onClick = {
                                    if (isFollowing) viewModel.removeFollowingUser(userDetail!!)
                                    else viewModel.addFollowingUser(userDetail!!)
                                },
                                modifier = Modifier.statusBarsPadding(),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = if (isFollowing) stringResource(id = R.string.following)
                                    else stringResource(id = R.string.follow)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
            ) { innerPadding ->

                var scrolledY = 0f
                var previousOffset = 0
                val lazyListState = rememberLazyListState()
                var selectedIndex by viewModel.selectedIndex

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding),
                    state = lazyListState,
                ) {
                    item {
                        UserInfoPage(
                            userDetail = userDetail!!,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .graphicsLayer {
                                    scrolledY +=
                                        lazyListState.firstVisibleItemScrollOffset - previousOffset
                                    translationY = scrolledY * 0.3f
                                    previousOffset = lazyListState.firstVisibleItemScrollOffset
                                }
                        )
                    }
                    stickyHeader {
                        TabRow(selectedTabIndex = selectedIndex) {
                            tabs.forEachIndexed { index, tabItem ->
                                Tab(
                                    selected = index == selectedIndex,
                                    onClick = { selectedIndex = index }) {
                                    Text(
                                        text = tabItem.description,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    when (tabs[selectedIndex].description) {
                        photosDescription -> {
                            items(count = userPhotos.size) { index ->
                                val photo = userPhotos[index]
                                PhotoComponent(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    photo = photo,
                                    showUserInfo = false,
                                    openPhoto = openPhoto
                                )
                            }
                        }
                        collectionsDescription -> {
                            items(count = userCollections.size) { index ->
                                val collection = userCollections[index]
                                CollectionCard(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = 16.dp),
                                    collectionId = collection.id,
                                    collectionTitle = collection.title,
                                    coverUrl = collection.coverPhoto.urls.regular,
                                    totalPhotos = collection.totalPhotos,
                                    onOpenCollection = openCollection
                                )
                            }
                        }
                        likesDescription -> {
                            items(count = userLikes.size) { index ->
                                val likes = userLikes[index]
                                PhotoComponent(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    photo = likes,
                                    showUserInfo = false,
                                    openPhoto = openPhoto
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
private fun UserInfoPage(modifier: Modifier = Modifier, userDetail: UserDetail) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = userDetail.profileImage.mediumScale,
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(200f)),
                contentScale = ContentScale.Crop,
            )
            DescriptionWithNumber(
                number = userDetail.totalPhotos,
                description = stringResource(id = R.string.photos)
            )
            DescriptionWithNumber(
                number = userDetail.totalCollections,
                description = stringResource(id = R.string.collections)
            )
            DescriptionWithNumber(
                number = userDetail.totalLikes,
                description = stringResource(id = R.string.likes)
            )
        }
        if (userDetail.bio != null) {
            Text(
                text = userDetail.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

    }
}

@Composable
private fun DescriptionWithNumber(number: Int, description: String) {
    Column {
        Text(
            text = number.toString(),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = description)
    }
}

private data class TabItem(val description: String, val icon: ImageVector, val isShow: Boolean)
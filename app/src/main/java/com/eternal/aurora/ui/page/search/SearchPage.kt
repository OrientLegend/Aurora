package com.eternal.aurora.ui.page.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.eternal.aurora.ui.theme.AuroraTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.eternal.aurora.R
import com.eternal.aurora.logic.model.PhotoSample
import com.eternal.aurora.logic.model.SearchUserInfo
import com.eternal.aurora.ui.page.main.category.CollectionCard
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
)
@Composable
fun SearchPage(
    viewModel: SearchViewModel = hiltViewModel(),
    openPhoto: (String) -> Unit,
    openCollection: (String, String, Int) -> Unit
) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val searchString by viewModel.searchString

    var isSearching by viewModel.isSearching


    val searchPhotoList by viewModel.searchPhotosState.collectAsState()
    val searchCollectionList by viewModel.searchCollectionsState.collectAsState()
    val searchUserList by viewModel.searchUsersState.collectAsState()

    val tabs = listOf(
        stringResource(id = R.string.photos),
        stringResource(id = R.string.collections),
        stringResource(id = R.string.users)
    )



    AuroraTheme {
        Scaffold(modifier = Modifier.statusBarsPadding(), topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SearchTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    //pagerState = pagerState
                )

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index: Int, text: String ->
                        Tab(selected = index == pagerState.currentPage, onClick = {

                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            if (searchString.isNotEmpty()) {
                                isSearching = true
                                when (index) {
                                    0 -> viewModel.searchPhotos(searchString = searchString)
                                    1 -> viewModel.searchCollections(searchString = searchString)
                                    2 -> viewModel.searchUsers(searchString = searchString)
                                }
                            }
                        }) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = if (pagerState.currentPage != index) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }) { innerPadding ->
            HorizontalPager(
                count = 3,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> {
                        SearchPhotoListPage(
                            photoList = searchPhotoList,
                            count = searchPhotoList.size,
                            onOpenPhoto = openPhoto,
                            isSearching = isSearching
                        )
                    }
                    1 -> {
                        LazyColumn {
                            items(items = searchCollectionList) { collection ->
                                CollectionCard(
                                    modifier = Modifier.padding(bottom = 20.dp),
                                    collectionId = collection.id,
                                    collectionTitle = collection.title,
                                    coverUrl = collection.coverPhoto.urls.regular,
                                    totalPhotos = collection.totalPhotos,
                                    onOpenCollection = openCollection
                                )
                            }
                        }
                    }
                    2 -> {
                        LazyColumn {
                            items(items = searchUserList) { userInfo: SearchUserInfo ->
                                UserItem(
                                    userInfo = userInfo,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    //pagerState: PagerState
) {
    var searchString by viewModel.searchString
    var isSearching by viewModel.isSearching
    val keyboard = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchString,
        onValueChange = { searchString = it },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.search)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            if (searchString.isNotEmpty()) {
                IconButton(onClick = { searchString = "" }) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel, contentDescription = null
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            isSearching = true
//            when (pagerState.currentPage) {
//                0 -> viewModel.searchPhotos(searchString = searchString)
//                1 -> viewModel.searchCollections(searchString = searchString)
//                2 -> viewModel.searchUsers(searchString = searchString)
//            }
            viewModel.apply {
                searchPhotos(searchString = searchString)
                searchCollections(searchString = searchString)
                searchUsers(searchString = searchString)
            }
            keyboard?.hide()
        })
    )
}

@Composable
private fun UserItem(modifier: Modifier = Modifier, userInfo: SearchUserInfo) {
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (userImage, username, userPhotos) = createRefs()
        AsyncImage(
            model = userInfo.profileImage.mediumScale,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(200f))
                .constrainAs(userImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = userInfo.name,
            modifier = Modifier
                .padding(start = 12.dp)
                .constrainAs(username) {
                    start.linkTo(userImage.end, 12.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(userImage.bottom)
                },
            style = MaterialTheme.typography.titleMedium
        )
        if (userInfo.totalPhotos > 0){
            UserPhotoSample(
                photoList = userInfo.photos,
                modifier = Modifier.constrainAs(userPhotos) {
                    start.linkTo(userImage.end)
                    top.linkTo(userImage.bottom)
                })
        }
    }
}

@Composable
private fun UserPhotoSample(
    modifier: Modifier = Modifier,
    photoList: List<PhotoSample>,
    spacerHeight: Dp = 0.dp,
    columnNumber: Int = 3,
) {
    LazyRow(
        modifier = modifier.padding(top = spacerHeight),
    ) {
        repeat(columnNumber) {
            item {
            }
        }
        items(count = photoList.size.coerceIn(0..3)) { index ->
            val photo = photoList[index]
            var showPlaceholder by remember {
                mutableStateOf(true)
            }
            val photoTransitionState =
                com.eternal.aurora.ui.page.photo.photoTransition(showPlaceholder = showPlaceholder)
            Column(
                modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20f))
                        .height(120.dp)
                        .width(100.dp)
                ) {
                    AsyncImage(
                        model = photo.urls.regular,
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(1f - photoTransitionState.alpha),
                        onState = { state: AsyncImagePainter.State ->
                            if (state is AsyncImagePainter.State.Success) {
                                showPlaceholder = false
                            }
                        }, contentScale = ContentScale.FillBounds
                    )


                    if (showPlaceholder) {
                        Image(
                            painter = ColorPainter(Color.Gray.copy(alpha = 0.5f)),
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(photoTransitionState.alpha)
                        )
                    }
                }
            }
        }
    }
}
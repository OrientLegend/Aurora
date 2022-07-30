package com.eternal.aurora.ui.page.photo

import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.eternal.aurora.App
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.UserInfo
import com.eternal.aurora.logic.model.encodeUrlsToUtf8
import com.eternal.aurora.ui.animation.LoadingAnimation
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

enum class PhotoListLayout {
    IsColumn, IsGrid
}

@Composable
fun LazyListState.OnAppearLastItem(onAppearLastItem: () -> Unit) {
    val isLastItemVisible by remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.size < layoutInfo.totalItemsCount &&
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isLastItemVisible }
            .filter { it }
            .collect {
                onAppearLastItem()
            }
    }
}

@Composable
fun LazyGridState.OnAppearLastItem(onAppearLastItem: () -> Unit) {
    val isLastItemVisible by remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.size < layoutInfo.totalItemsCount &&
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isLastItemVisible }
            .filter { it }
            .collect {
                onAppearLastItem()
            }
    }
}

@Composable
fun PhotoListPage(
    modifier: Modifier = Modifier,
    photoList: List<Photo>,
    count: Int,
    showUserInfo: Boolean = true,
    onOpenPhoto: (String) -> Unit,
    spacerHeight: Dp = 0.dp,
    photoListLayout: PhotoListLayout = PhotoListLayout.IsColumn,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    loadMore: () -> Unit = {},
    openUser: (String) -> Unit = {},
    showLoadingAnimation: Boolean = true
) {

    if (photoList.isEmpty() && showLoadingAnimation) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
        }
    }


    if (photoListLayout == PhotoListLayout.IsColumn) {
        lazyListState.OnAppearLastItem(loadMore)

        LazyColumn(modifier = modifier, state = lazyListState) {
            item {
                Spacer(modifier = Modifier.height(spacerHeight))
            }
            items(count = count) { index ->
                val photo = photoList[index]
                PhotoComponent(
                    photo = photo,
                    showUserInfo = showUserInfo,
                    openUser = openUser,
                    openPhoto = onOpenPhoto
                )
            }
        }
    } else {
        lazyGridState.OnAppearLastItem(loadMore)
        LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2), state = lazyGridState) {
            repeat(2) {
                item {
                    Spacer(modifier = Modifier.height(spacerHeight))
                }
            }
            items(count = count) { index ->
                val photo = photoList[index]
                var showPlaceholder by remember {
                    mutableStateOf(true)
                }
                val photoTransitionState = photoTransition(showPlaceholder = showPlaceholder)
                val photoWidth =
                    (LocalConfiguration.current.screenWidthDp.toFloat() - 32f) / 2 //subtract the padding
                //val photoHeight = photoWidth * photo.height / photo.width
                Column(modifier = Modifier.padding(bottom = 16.dp).run {
                    if (index and 1 == 0) {
                        padding(end = 8.dp)
                    } else {
                        padding(start = 8.dp)
                    }
                }) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20f))
                            .width(photoWidth.dp)
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = photo.urls.regular,
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(1f - photoTransitionState.alpha)
                                .clickable { onOpenPhoto(Gson().toJson(photo.encodeUrlsToUtf8())) },
                            onState = { state: AsyncImagePainter.State ->
                                if (state is AsyncImagePainter.State.Success) {
                                    showPlaceholder = false
                                }
                            }, contentScale = ContentScale.FillHeight
                        )

                        val colorNumber = photo.color.toInt(16) + (0xFF shl 24)

                        Image(
                            painter = ColorPainter(Color(colorNumber).copy(alpha = 0.5f)),
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

@Composable
fun PhotoComponent(
    modifier: Modifier = Modifier,
    photo: Photo,
    showUserInfo: Boolean,
    openUser: (String) -> Unit = { },
    openPhoto: (String) -> Unit = { }
) {
    var showPlaceholder by remember {
        mutableStateOf(true)
    }
    val photoTransitionState = photoTransition(showPlaceholder = showPlaceholder)
    val photoWidth =
        LocalConfiguration.current.screenWidthDp.toFloat() - 32f //subtract the padding
    val photoHeight = photoWidth * photo.height / photo.width
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        if (showUserInfo) {
            UserWithImage(
                userInfo = photo.user,
                modifier = Modifier.padding(bottom = 5.dp),
                openUser = openUser
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20f))
                .height(photoHeight.dp)
        ) {
            AsyncImage(model = photo.urls.regular,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(1f - photoTransitionState.alpha)
                    .clickable { openPhoto(Gson().toJson(photo.encodeUrlsToUtf8())) },
                onState = { state: AsyncImagePainter.State ->
                    if (state is AsyncImagePainter.State.Success) {
                        showPlaceholder = false
                    }
                })

            val colorNumber = photo.color.toInt(16) + (0xFF shl 24)

            Image(
                painter = ColorPainter(Color(colorNumber).copy(alpha = 0.5f)),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(photoTransitionState.alpha)
            )
        }
    }
}

@Composable
fun UserWithImage(userInfo: UserInfo, modifier: Modifier = Modifier, openUser: (String) -> Unit) {
    Row(modifier = modifier.clickable {
        openUser(userInfo.username)
    }) {
        AsyncImage(
            model = userInfo.profileImage.mediumScale,
            contentDescription = userInfo.name,
            modifier = Modifier
                .size(48.dp)
                .clip(
                    RoundedCornerShape(100f)
                )
        )
        Text(
            text = userInfo.name,
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun photoTransition(showPlaceholder: Boolean): PhotoTransition {
    val transition = updateTransition(targetState = showPlaceholder, label = "transition")
    val placeholderAlpha = transition.animateFloat(label = "placeholderAlpha") { state ->
        when (state) {
            true -> 1f
            false -> 0f
        }
    }
    return PhotoTransition(placeholderAlpha = placeholderAlpha)
}

class PhotoTransition(placeholderAlpha: State<Float>) {
    val alpha by placeholderAlpha
}
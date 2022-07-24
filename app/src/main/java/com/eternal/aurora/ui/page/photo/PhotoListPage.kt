package com.eternal.aurora.ui.page.photo

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.UserInfo
import com.eternal.aurora.logic.model.encodeUrlsToUtf8
import com.eternal.aurora.ui.animation.LoadingAnimation
import com.google.gson.Gson

@Composable
fun PhotoListPage(modifier: Modifier = Modifier ,photoList: List<Photo>, onOpenPhoto: (String) -> Unit) {
    val photoWidth = LocalConfiguration.current.screenWidthDp.toFloat() - 32f //subtract the padding

    if (photoList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
        }
    }

    LazyColumn(modifier = modifier) {
        items(count = photoList.size) { index ->
            val photo = photoList[index]
            var showPlaceholder by remember {
                mutableStateOf(true)
            }
            val photoTransitionState = photoTransition(showPlaceholder = showPlaceholder)
            val photoHeight = photoWidth * photo.height / photo.width
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                UserWithImage(userInfo = photo.user, modifier = Modifier.padding(bottom = 5.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20f))
                        .height(photoHeight.dp)
                ) {
                    AsyncImage(
                        model = photo.urls.regular, contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(1f - photoTransitionState.alpha)
                            .clickable { onOpenPhoto(Gson().toJson(photo.encodeUrlsToUtf8())) },
                        onState = { state: AsyncImagePainter.State ->
                            if (state is AsyncImagePainter.State.Success) {
                                showPlaceholder = false
                            }
                        }
                    )

                    val colorNumber = photo.color.toInt(16) +
                            (0xFF shl 24)

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

@Composable
fun UserWithImage(userInfo: UserInfo, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
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
private fun photoTransition(showPlaceholder: Boolean): PhotoTransition {
    val transition = updateTransition(targetState = showPlaceholder, label = "transition")
    val placeholderAlpha = transition.animateFloat(label = "placeholderAlpha") { state ->
        when (state) {
            true -> 1f
            false -> 0f
        }
    }
    return PhotoTransition(placeholderAlpha = placeholderAlpha)
}

private class PhotoTransition(placeholderAlpha: State<Float>) {
    val alpha by placeholderAlpha
}
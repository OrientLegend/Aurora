package com.eternal.aurora.ui.page.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Exposure
import androidx.compose.material.icons.filled.Iso
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.eternal.aurora.App
import com.eternal.aurora.R
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.PhotoDetail
import com.eternal.aurora.logic.model.PhotoLocation
import com.google.gson.Gson
import com.eternal.aurora.ui.animation.LoadingAnimation
import com.eternal.aurora.ui.component.BottomDrawer
import com.eternal.aurora.ui.page.main.home.UserWithImage
import com.eternal.aurora.ui.theme.AuroraTheme
import com.eternal.aurora.ui.utils.saveToAlbum
import com.eternal.aurora.ui.utils.urlToBitmap
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoPage(photoJson: String, photoViewModel: PhotoViewModel = hiltViewModel()) {
    val photo = Gson().fromJson(photoJson, Photo::class.java)

    photoViewModel.getPhotoDetail(photo.id)

    val photoDetailState = photoViewModel.photoDetailState.collectAsState()

    var isPhotoLoaded by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val coroutineScope = rememberCoroutineScope()

    AuroraTheme(statusBarColor = Color.Black.copy(alpha = 0.3f), statusBarDarkIcon = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            AsyncImage(
                model = photo.urls.full,
                contentDescription = null,
                onState = { state: AsyncImagePainter.State ->
                    if (state is AsyncImagePainter.State.Success) {
                        isPhotoLoaded = true
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        coroutineScope.launch {
                            drawerState.show()
                        }
                    },
                contentScale = ContentScale.FillHeight
            )

            if (!isPhotoLoaded) {
                Image(
                    painter = ColorPainter(Color.Black),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )
                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            }

            BottomSheetDialog(
                photo = photo,
                drawerState = drawerState,
                photoDetailState = photoDetailState,
                coroutineScope = coroutineScope
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetDialog(
    photo: Photo,
    modifier: Modifier = Modifier,
    drawerState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    ),
    photoDetailState: State<PhotoDetail?>,
    coroutineScope: CoroutineScope
) {
    BottomDrawer(modifier = modifier, drawerState = drawerState, sheetContent = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (userInfo, iconFileDownload, iconFavorite) = createRefs()
                UserWithImage(userInfo = photo.user, modifier = Modifier.constrainAs(userInfo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.constrainAs(iconFavorite) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }, tint = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val url = URL(photo.urls.regular)
                            url.urlToBitmap()?.saveToAlbum(App.context, "test.jpg")
                        }
                    },
                    modifier = Modifier.constrainAs(iconFileDownload) {
                        end.linkTo(iconFavorite.start, margin = 12.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.FileDownload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }


            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 10.dp)
            )

            FlowRow {
                val photoDetail = photoDetailState.value
                val exif = photoDetail?.exif
                val location = photoDetail?.location
                Introduction(
                    name = stringResource(id = R.string.camera),
                    description = exif?.name,
                    imageVector = Icons.Outlined.PhotoCamera,
                    modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)
                )
                Introduction(
                    name = stringResource(id = R.string.focal_length),
                    description = if (exif?.focalLength != null) exif.focalLength.toString() else null,
                    supplement = "mm",
                    imageVector = Icons.Outlined.Visibility,
                )
                Introduction(
                    name = stringResource(id = R.string.shutter_speed),
                    description = exif?.exposureTime,
                    supplement = "s",
                    imageVector = Icons.Filled.Exposure
                )
                Introduction(
                    name = stringResource(id = R.string.iso),
                    description = exif?.iso,
                    imageVector = Icons.Filled.Iso
                )
                Introduction(
                    name = stringResource(id = R.string.dimensions),
                    description = "${photo.width.toInt()}×${photo.height.toInt()}",
                    imageVector = Icons.Outlined.PhotoSizeSelectActual
                )
                Introduction(
                    name = stringResource(id = R.string.location),
                    description = getLocationDescription(location),
                    imageVector = Icons.Outlined.Place
                )
            }
        }
    })
}

@Composable
private fun Introduction(
    modifier: Modifier = Modifier,
    name: String,
    description: String?,
    supplement: String = "",
    imageVector: ImageVector,
) {
    if (description != null) { //If description is null, then it's unnecessary to show
        Row(modifier = modifier.padding(6.dp)) {
            Icon(
                imageVector = imageVector,
                contentDescription = name,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "${description}$supplement",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
        }
    }
}

private fun getLocationDescription(location: PhotoLocation?): String? {
    if (location?.country == null) return null
    if (location.city == null) return location.country
    return "${location.country} ${location.city}"
}
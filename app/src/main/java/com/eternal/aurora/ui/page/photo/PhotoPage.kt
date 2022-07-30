package com.eternal.aurora.ui.page.photo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
fun PhotoPage(
    photoJson: String,
    photoViewModel: PhotoViewModel = hiltViewModel(),
    openUser: (String) -> Unit
) {
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
                coroutineScope = coroutineScope,
                openUser = openUser
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
    viewModel: PhotoViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope,
    openUser: (String) -> Unit
) {
    val photoDetailState = viewModel.photoDetailState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState().value.collectAsState(initial = false)
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
                }, openUser = openUser)

                IconButton(
                    onClick = {
                        if (isFavorite) viewModel.removeFavoritePhoto(photo)
                        else viewModel.addFavoritePhoto(photo)
                    },
                    modifier = Modifier.constrainAs(iconFavorite) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val url = URL(photo.urls.regular)
                            val defaultFileName = photo.id
                            url.urlToBitmap()?.saveToAlbum(App.context, "$defaultFileName.jpg")
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

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
                val photoDetail = photoDetailState.value
                val exif = photoDetail?.exif
                val location = photoDetail?.location
                introduction(
                    nameId = R.string.camera,
                    description = exif?.name,
                    imageVector = Icons.Outlined.PhotoCamera,
                    modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)
                )
                introduction(
                    nameId = R.string.focal_length,
                    description = if (exif?.focalLength != null) exif.focalLength.toString() else null,
                    supplement = "mm",
                    imageVector = Icons.Outlined.Visibility,
                )
                introduction(
                    nameId = R.string.shutter_speed,
                    description = exif?.exposureTime,
                    supplement = "s",
                    imageVector = Icons.Filled.Exposure
                )
                introduction(
                    nameId = R.string.iso,
                    description = exif?.iso,
                    imageVector = Icons.Filled.Iso
                )
                introduction(
                    nameId = R.string.dimensions,
                    description = "${photo.width.toInt()}×${photo.height.toInt()}",
                    imageVector = Icons.Outlined.PhotoSizeSelectActual
                )
                introduction(
                    nameId = R.string.location,
                    description = getLocationDescription(location),
                    imageVector = Icons.Outlined.Place
                )
            }
        }
    })
}

private fun LazyGridScope.introduction(
    modifier: Modifier = Modifier,
    nameId: Int,
    description: String?,
    supplement: String = "",
    imageVector: ImageVector,
) {
    if (description != null) { //If description is null, then it's unnecessary to show
        item {
            Row(modifier = modifier.padding(6.dp)/*.aspectRatio(2.5f)*/) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = stringResource(id = nameId),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = stringResource(id = nameId),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${description}$supplement",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun getLocationDescription(location: PhotoLocation?): String? {
    if (location?.country == null) return null
    if (location.city == null) return location.country
    return "${location.country} ${location.city}"
}
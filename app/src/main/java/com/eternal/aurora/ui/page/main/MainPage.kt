package com.eternal.aurora.ui.page.main

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eternal.aurora.R
import com.eternal.aurora.ui.page.main.category.CategoryPage
import com.eternal.aurora.ui.page.main.favorite.FavoritePage
import com.eternal.aurora.ui.page.main.home.HomePage
import com.eternal.aurora.ui.page.main.settings.SettingsPage
import com.eternal.aurora.ui.theme.AuroraTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(onOpenPhoto: (String) -> Unit, onOpenCollection: (String) -> Unit) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val pageList =
        listOf<@Composable () -> Unit>(
            { HomePage(onOpenPhoto = onOpenPhoto) },
            { CategoryPage(onOpenCollection = onOpenCollection) },
            { FavoritePage() },
            { SettingsPage() })

    AuroraTheme {
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding(),
            topBar = { TopBar() },
            bottomBar = {
                NavigationBar {
                    tabs.forEachIndexed { index, tabItem ->
                        NavigationBarItem(
                            selected = index == pagerState.currentPage,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tabItem.icon,
                                    contentDescription = stringResource(id = tabItem.stringId)
                                )
                            },
                            label = { Text(text = stringResource(id = tabItem.stringId)) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            HorizontalPager(
                count = pageList.size,
                state = pagerState,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) { page ->
                pageList[page]()
            }
        }
    }

}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
        )
    }
}


private val tabs =
    listOf(
        TabItem(Icons.Filled.Home, R.string.home),
        TabItem(Icons.Filled.Category, R.string.category),
        TabItem(Icons.Filled.Favorite, R.string.favorite),
        TabItem(Icons.Filled.Settings, R.string.settings)
    )


private data class TabItem(val icon: ImageVector, @StringRes val stringId: Int)
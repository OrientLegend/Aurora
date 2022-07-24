package com.eternal.aurora.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eternal.aurora.ui.page.collection.CollectionPage
import com.eternal.aurora.ui.page.main.MainPage
import com.eternal.aurora.ui.page.photo.PhotoListPage
import com.eternal.aurora.ui.page.photo.PhotoPage

object MainDestinations {
    const val MAIN_ROUTE = "main_route"
    const val PHOTO_ROUTE = "photo_route"
    const val COLLECTION_ROUTE = "collection_route"
}


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.MAIN_ROUTE
) {

    val actions = remember(key1 = navController) {
        MainActions(navController)
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(MainDestinations.MAIN_ROUTE) {
            MainPage(onOpenPhoto = actions.openPhoto, onOpenCollection = actions.openPhotoCollection)
        }

        composable("${MainDestinations.PHOTO_ROUTE}/{photo}") {
            PhotoPage(photoJson = it.arguments?.getString("photo", "") ?: "")
        }

        composable("${MainDestinations.COLLECTION_ROUTE}/{collection_id}") {
            CollectionPage(
                collectionId = it.arguments?.getString("collection_id", "") ?: "",
                onOpenPhoto = actions.openPhoto
            )
        }
    }
}


class MainActions(navController: NavHostController) {

    val openPhoto = { photoJson: String ->
        navController.navigate("${MainDestinations.PHOTO_ROUTE}/$photoJson")
    }

    val openPhotoCollection = { collectionId: String ->
        navController.navigate("${MainDestinations.COLLECTION_ROUTE}/$collectionId")
    }
}

//private fun NavBackStackEntry.lifecycleIsResumed() = 
//    this.lifecycle.currentState == Lifecycle.State.RESUMED
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
import com.eternal.aurora.ui.page.photo.PhotoPage
import com.eternal.aurora.ui.page.search.SearchPage
import com.eternal.aurora.ui.page.topic.TopicPage
import com.eternal.aurora.ui.page.user.UserPage

object MainDestinations {
    const val MAIN_ROUTE = "main_route"
    const val PHOTO_ROUTE = "photo_route"
    const val COLLECTION_ROUTE = "collection_route"
    const val TOPIC_ROUTE = "topic_route"
    const val SEARCH_ROUTE = "search_route"
    const val USER_ROUTE = "user_route"
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
            MainPage(
                onOpenPhoto = actions.openPhoto,
                onOpenCollection = actions.openPhotoCollection,
                onOpenTopic = actions.openPhotoTopic,
                openSearchPage = actions.openSearchPage,
                openUser = actions.openUser
            )
        }

        composable("${MainDestinations.PHOTO_ROUTE}/{photo}") {
            PhotoPage(
                photoJson = it.arguments?.getString("photo", "") ?: "",
                openUser = actions.openUser
            )
        }

        composable(
            "${MainDestinations.COLLECTION_ROUTE}/{collection_id}/{collection_name}/{total_photos}",
            arguments = listOf(
                navArgument("total_photos") { type = NavType.IntType }
            )
        ) {
            CollectionPage(
                collectionId = it.arguments?.getString("collection_id", "") ?: "",
                collectionName = it.arguments?.getString("collection_name", "") ?: "",
                totalPhotos = it.arguments?.getInt("total_photos", 0) ?: 0,
                onOpenPhoto = actions.openPhoto,
                upPress = {
                    actions.upPress(it)
                }
            )
        }

        composable(
            "${MainDestinations.TOPIC_ROUTE}/{topic_id}/{topic_name}/{total_photos}",
            arguments = listOf(
                navArgument("total_photos") { type = NavType.IntType }
            )
        ) {
            TopicPage(
                topicId = it.arguments?.getString("topic_id", "") ?: "",
                topicName = it.arguments?.getString("topic_name", "") ?: "",
                totalPhotos = it.arguments?.getInt("total_photos", 0) ?: 0,
                onOpenPhoto = actions.openPhoto,
                upPress = { actions.upPress(it) })
        }

        composable(MainDestinations.SEARCH_ROUTE) {
            SearchPage(
                openPhoto = actions.openPhoto,
                openCollection = actions.openPhotoCollection
            )
        }

        composable("${MainDestinations.USER_ROUTE}/{username}") {
            UserPage(username = it.arguments?.getString("username", "") ?: "", upPress = {
                actions.upPress(it)
            }, openPhoto = actions.openPhoto, openCollection = actions.openPhotoCollection)
        }
    }
}


class MainActions(navController: NavHostController) {

    val openPhoto = { photoJson: String ->
        navController.navigate("${MainDestinations.PHOTO_ROUTE}/$photoJson")
    }

    val openPhotoCollection =
        { collectionId: String, collectionName: String, totalPhotos: Int ->
            navController.navigate("${MainDestinations.COLLECTION_ROUTE}/$collectionId/$collectionName/$totalPhotos")
        }

    val openPhotoTopic =
        { topicId: String, topicName: String, totalPhotos: Int ->
            navController.navigate("${MainDestinations.TOPIC_ROUTE}/$topicId/$topicName/$totalPhotos")
        }

    val openSearchPage = {
        navController.navigate(MainDestinations.SEARCH_ROUTE)
    }

    val openUser = { username: String ->
        navController.navigate("${MainDestinations.USER_ROUTE}/$username")
    }

    val upPress: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }

}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
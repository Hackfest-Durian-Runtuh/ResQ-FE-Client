package com.example.resq.global_component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.resq.mainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoadingLayout(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    val loadingState = rememberSwipeRefreshState(
        isRefreshing = mainViewModel.loading.value
    )

    SwipeRefresh(
        modifier = modifier,
        state = loadingState,
        onRefresh = { /*TODO*/ },
        swipeEnabled = false
    ) {
        content()

        AnimatedContent(
            targetState = loadingState.isRefreshing
        ) { refreshing ->
            if (refreshing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = .5f))
                )
            }
        }
    }
}
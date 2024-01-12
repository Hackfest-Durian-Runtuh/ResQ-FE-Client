package com.example.resq.global_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun NonLazyVerticalGrid(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 0.dp,
    columnCount: Int,
    eachItemAlignment: Alignment = Alignment.TopCenter,
    containerWidth:Dp? = null,
    containerHorizontalPadding:Dp? = null,
    content: NonLazyGridScope.() -> Unit,
) {
    val scope = NonLazyGridScope()
    content(scope)

    val maxRow = ceil(scope.contents.size.toFloat() / columnCount.toFloat())
    val maxColWidth = (containerWidth ?: (LocalConfiguration.current.screenWidthDp - ((containerHorizontalPadding?.value ?: 0f) * 2)).dp)/columnCount

    Column(
        modifier = modifier,
    ) {
        for (i in 0 until maxRow.toInt()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = containerHorizontalPadding ?: 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (j in 0 until columnCount) {
                    val arrIndex = j + i * columnCount

                    if (arrIndex <= scope.contents.size - 1) {
                        Box(modifier = Modifier.widthIn(max = maxColWidth), contentAlignment = eachItemAlignment){
                            scope.contents[arrIndex]()
                        }
                    }else{
                        Box(modifier = Modifier.width(maxColWidth))
                    }
                }
            }

            if (i < maxRow.toInt() - 1) {
                Spacer(modifier = Modifier.height(verticalSpacing))
            }
        }
    }
}

class NonLazyGridScope {
    val contents = ArrayList<@Composable (() -> Unit)>()

    fun item(
        content: @Composable (() -> Unit)
    ) {
        contents.add(content)
    }
}
package com.vgleadsheets.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.composables.previews.BigImageConstants
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.composables.previews.NotifConstants
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.SheetConstants
import com.vgleadsheets.composables.previews.SquareConstants
import com.vgleadsheets.composables.previews.WideItemConstants
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.composables.subs.Flasher
import com.vgleadsheets.model.generator.StringGenerator
import java.util.Random
import kotlin.random.asKotlinRandom
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("MagicNumber")
fun LoadingItem(
    seed: Long,
    loadingType: LoadingType,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val randomizer = Random(seed)
    val randomDelay = randomizer.nextInt(200)

    val (width, ratio) = when (loadingType) {
        LoadingType.SHEET -> SheetConstants.MIN_WIDTH to SheetConstants.ASPECT_RATIO
        LoadingType.SQUARE -> SquareConstants.MIN_WIDTH to SquareConstants.ASPECT_RATIO
        LoadingType.NOTIF -> NotifConstants.MIN_WIDTH to NotifConstants.ASPECT_RATIO
        LoadingType.WIDE_ITEM -> WideItemConstants.MIN_WIDTH to WideItemConstants.ASPECT_RATIO
        LoadingType.BIG_IMAGE -> BigImageConstants.MIN_WIDTH to BigImageConstants.ASPECT_RATIO
        else -> return
    }

    ElevatedRoundRect(
        modifier = modifier
            .padding(paddingValues = padding)
            .padding(bottom = 4.dp, top = 4.dp)
            .defaultMinSize(minWidth = width)
            .aspectRatio(ratio),
    ) {
        Flasher(startDelay = randomDelay)
    }
}

@Preview
@Composable
private fun Light() {
    val randomizer = Random(RANDOMIZER_SEED)
    val stringGen = StringGenerator(randomizer)
    FullScreenOf { paddingValues ->
        Sample(
            randomizer = randomizer,
            paddingValues = paddingValues,
            stringGen = stringGen,
        )
    }
}

@Preview
@Composable
private fun Dark() {
    val randomizer = Random(RANDOMIZER_SEED)
    val stringGen = StringGenerator(randomizer)
    FullScreenOf(darkTheme = true) { paddingValues ->
        Sample(
            randomizer = randomizer,
            paddingValues = paddingValues,
            stringGen = stringGen,
        )
    }
}

@Composable
@Suppress("MagicNumber")
private fun ColumnScope.Sample(
    randomizer: Random,
    paddingValues: PaddingValues,
    stringGen: StringGenerator,
) {
    val possibleTypes = listOf(
        LoadingType.SHEET,
        LoadingType.SQUARE,
        LoadingType.NOTIF,
        LoadingType.WIDE_ITEM,
        LoadingType.BIG_IMAGE
    )

    val loadingType = possibleTypes.random(randomizer.asKotlinRandom())
    val rowName = loadingType.name
    LoadingSectionHeader(
        seed = randomizer.nextLong(),
        modifier = Modifier,
        padding = paddingValues
    )

    val items = List(randomizer.nextInt(5) + 5) { index ->
        LoadingItemListModel(
            loadingType,
            rowName,
            index,
        )
    }.toImmutableList()

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = "$rowName.content".hashCode().toLong(),
            scrollingItems = items,
        ),
        actionSink = PreviewActionSink(),
        modifier = Modifier,
        padding = paddingValues
    )
}

private const val RANDOMIZER_SEED = 1231L

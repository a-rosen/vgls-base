package com.vgleadsheets.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.composables.subs.ElevatedCircle
import com.vgleadsheets.composables.subs.ElevatedPill
import com.vgleadsheets.composables.subs.Flasher
import com.vgleadsheets.composables.utils.nextPercentageFloat
import kotlin.random.Random

@Composable
@Suppress("MagicNumber", "LongMethod")
fun LoadingTextItem(
    loadingType: LoadingType,
    seed: Long,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val (withImage, withCaption) = when (loadingType) {
        LoadingType.SINGLE_TEXT -> false to false
        LoadingType.TEXT_CAPTION -> false to true
        LoadingType.TEXT_IMAGE -> true to false
        LoadingType.TEXT_CAPTION_IMAGE -> true to true
        else -> return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
    ) {
        val randomizer = Random(seed)
        val randomDelay = randomizer.nextInt(200)

        if (withImage) {
            ElevatedCircle(
                Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Flasher(startDelay = randomDelay)
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val paddingModifier = if (withCaption) {
                Modifier.padding(top = 12.dp)
            } else {
                Modifier.padding(vertical = 16.dp)
            }
            ElevatedPill(
                modifier = paddingModifier
                    .height(14.dp)
                    .fillMaxWidth(
                        randomizer.nextPercentageFloat(
                            minOutOfHundred = 30,
                        )
                    )
            ) {
                Flasher(startDelay = randomDelay)
            }

            if (withCaption) {
                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                ElevatedPill(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 12.dp)
                        .height(10.dp)
                        .fillMaxWidth(
                            randomizer.nextPercentageFloat(
                                minOutOfHundred = 30,
                            )
                        )
                ) {
                    Flasher(startDelay = randomDelay)
                }
            }
        }
    }
}

@Preview
@Composable
private fun Light() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            loadingType = LoadingType.SINGLE_TEXT,
        )
    }
}

@Preview
@Composable
private fun LightWithImage() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            loadingType = LoadingType.TEXT_IMAGE,
        )
    }
}

@Preview
@Composable
private fun LightWithImageAndCaption() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        )
    }
}

@Preview
@Composable
private fun DarkWithImageAndCaption() {
    FullScreenOf(darkTheme = true) {
        Sample(
            seed = Random.nextLong(),
            loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        )
    }
}

@Composable
private fun Sample(seed: Long, loadingType: LoadingType) {
    LoadingTextItem(
        loadingType = loadingType,
        seed = seed,
        modifier = Modifier,
        padding = PaddingValues(horizontal = 16.dp)
    )
}

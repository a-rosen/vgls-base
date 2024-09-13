package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.SmallTextListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SmallText(
    model: SmallTextListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding)
            .padding(bottom = 8.dp, top = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable { actionSink.sendAction(model.clickAction) }
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        val color = MaterialTheme.colorScheme.onSurfaceVariant

        Text(
            text = model.name,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Composable
@Suppress("MagicNumber", "LongMethod")
private fun Sample() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val padding = PaddingValues(horizontal = 16.dp)
        val actionSink = PreviewActionSink { }

        val seed = 1234L
        val random = Random(seed)
        val stringGenerator = StringGenerator(random)

        repeat(15) {
            val rowName = stringGenerator.generateTitle()

            SectionHeader(
                name = rowName,
                modifier = Modifier,
                padding = padding
            )

            HorizontalScroller(
                model = HorizontalScrollerListModel(
                    dataId = rowName.hashCode().toLong(),
                    scrollingItems = List(15) {
                        val name = stringGenerator.generateName()
                        SmallTextListModel(
                            name = name,
                            clickAction = VglsAction.Noop,
                        )
                    }.toImmutableList()
                ),
                actionSink = actionSink,
                modifier = Modifier,
                padding = padding
            )
        }
    }
}

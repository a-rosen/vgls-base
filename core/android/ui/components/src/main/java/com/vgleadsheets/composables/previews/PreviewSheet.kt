package com.vgleadsheets.composables.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vgleadsheets.bitmaps.R
import com.vgleadsheets.images.LoadingIndicatorConfig

@Composable
fun PreviewSheet(
    loadingIndicatorConfig: LoadingIndicatorConfig,
    fillMaxWidth: Boolean,
    modifier: Modifier
) {
    val actualModifierLol = if (fillMaxWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier.width(300.dp)
    }

    Box(
        modifier = actualModifierLol
            .aspectRatio(SheetConstants.ASPECT_RATIO)
            .background(Color.White)
    ) {
        if (loadingIndicatorConfig.pageNumber == 0) {
            PreviewSheetTitle(
                loadingIndicatorConfig,
                modifier
            )
        } else {
            PreviewSheetOther()
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun BoxScope.PreviewSheetTitle(
    loadingIndicatorConfig: LoadingIndicatorConfig,
    modifier: Modifier = Modifier
) {
    Text(
        text = loadingIndicatorConfig.title,
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 12.dp)
    )

    val gameName = loadingIndicatorConfig.gameName
    if (gameName.isNotEmpty()) {
        Text(
            text = "from $gameName",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 10.sp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )
    }

    Text(
        text = "https://www.vgleadsheets.com/",
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 8.sp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
    )

    Column(
        modifier = Modifier
            .padding(top = 72.dp)
            .padding(horizontal = 24.dp)
    ) {
        repeat(10) {
            Image(
                painter = painterResource(R.drawable.img_leadsheet_single_system_blank),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun BoxScope.PreviewSheetOther() {
    Text(
        text = "https://www.vgleadsheets.com/",
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 8.sp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
    )

    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 24.dp)
    ) {
        repeat(11) {
            Image(
                painter = painterResource(R.drawable.img_leadsheet_single_system_blank),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

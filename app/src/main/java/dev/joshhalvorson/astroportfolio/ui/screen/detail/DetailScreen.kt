package dev.joshhalvorson.astroportfolio.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.joshhalvorson.astroportfolio.ui.components.FullsizeAstroImage
import dev.joshhalvorson.data.gallery.AstroData

@Composable
fun DetailScreen(image: AstroData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FullsizeAstroImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            url = image.fullSize,
            contentScale = ContentScale.Inside
        )

        Text(text = image.name)
        Text(text = image.date)
        Text(text = image.type)
    }
}

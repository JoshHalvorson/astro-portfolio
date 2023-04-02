package dev.joshhalvorson.astroportfolio.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun FullsizeAstroImage(
    modifier: Modifier = Modifier,
    url: String,
    filterQuality: FilterQuality = FilterQuality.High,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    AstroImage(
        modifier = modifier,
        url = url,
        size = null,
        filterQuality = filterQuality,
        shape = RoundedCornerShape(0),
        contentScale = contentScale
    )
}

@Composable
fun ThumbnailAstroImage(
    url: String,
    size: Dp = 50.dp,
    filterQuality: FilterQuality = FilterQuality.Low,
    contentScale: ContentScale = ContentScale.Crop
) {
    AstroImage(
        url = url,
        size = size,
        shape = RoundedCornerShape(5.dp),
        filterQuality = filterQuality,
        contentScale = contentScale
    )
}

@Composable
private fun AstroImage(
    modifier: Modifier = Modifier,
    url: String,
    size: Dp?,
    shape: CornerBasedShape,
    filterQuality: FilterQuality,
    contentScale: ContentScale
) {
    var imageLoading by rememberSaveable { mutableStateOf(true) }
    val sizeModifier = if (size != null) Modifier.size(size) else modifier

    AsyncImage(
        modifier = sizeModifier
            .clip(shape)
            .placeholder(
                visible = imageLoading,
                highlight = PlaceholderHighlight.shimmer(),
            ),
        model = url,
        contentDescription = null,
        contentScale = contentScale,
        filterQuality = filterQuality,
        alignment = Alignment.TopCenter,
        onState = {
            imageLoading = it is AsyncImagePainter.State.Loading
        }
    )
}
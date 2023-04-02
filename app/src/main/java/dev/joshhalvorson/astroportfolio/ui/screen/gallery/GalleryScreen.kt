package dev.joshhalvorson.astroportfolio.ui.screen.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.joshhalvorson.astroportfolio.ui.components.AstroContainer
import dev.joshhalvorson.astroportfolio.ui.components.AstroInfo
import dev.joshhalvorson.astroportfolio.ui.components.ThumbnailAstroImage
import dev.joshhalvorson.astroportfolio.ui.theme.AstroportfolioTheme
import dev.joshhalvorson.data.gallery.AstroData

@Suppress("DEPRECATION")
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = hiltViewModel(),
    onImageClicked: (id: String) -> Unit
) {
    val astroData by viewModel.data.collectAsState()
    val loading by viewModel.loading.collectAsState()

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(loading),
        onRefresh = { viewModel.getData() }
    ) {
        Column {
            if (loading) {
                LoadingList()
            } else {
                ImagesList(data = astroData, onImageClicked = onImageClicked)
            }
        }
    }
}

@Composable
private fun ImagesList(data: List<AstroData>, onImageClicked: (id: String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(data, key = { _, item -> item.id }) { index, it ->
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                AstroContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onImageClicked(it.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AstroInfo(
                            modifier = Modifier.weight(1f),
                            name = it.name,
                            type = it.type
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        ThumbnailAstroImage(url = it.thumbnail)
                    }
                }

                if (index != data.size - 1) {
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun LoadingList() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed((1..6).toList()) { _, item ->
            Column {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            modifier = Modifier
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer()
                                ),
                            text = "Placeholder text"
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Surface(
                            modifier = Modifier.placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "type",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }

                if (item != 6) {
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoadingList() {
    AstroportfolioTheme {
        LoadingList()
    }
}
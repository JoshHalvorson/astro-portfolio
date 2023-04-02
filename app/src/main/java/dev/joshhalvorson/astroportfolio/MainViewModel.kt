package dev.joshhalvorson.astroportfolio

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.joshhalvorson.data.gallery.AstroData
import dev.joshhalvorson.data.gallery.GalleryRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {
    fun getImage(id: String): AstroData {
        return repository.getImage(id)
    }
}
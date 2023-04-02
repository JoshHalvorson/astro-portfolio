package dev.joshhalvorson.astroportfolio.ui.screen.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.joshhalvorson.data.AstroFolder
import dev.joshhalvorson.data.gallery.AstroData
import dev.joshhalvorson.data.gallery.GalleryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {
    private val _data = MutableStateFlow(emptyList<AstroData>())
    val data = _data.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            launch {
                repository.getAllFoldersInStorage()
                    .onStart {
                        Log.i("galleryViewModel", "getAllFoldersInStorage start")
                        _loading.emit(true)
                    }
                    .catch {
                        Log.e("galleryViewModel", "getAllFoldersInStorage error", it)
                        _loading.emit(false)
                    }
                    .flatMapLatest {
                        Log.i("galleryViewModel", "folders: $it")
                        repository.getDataFileForFolders(folders = it)
                    }
                    .flatMapLatest {
                        Log.i("galleryViewModel", "data: $it")
                        repository.getDownloadUrlForImage(it)
                    }
                    .collect {
                        Log.i("galleryViewModel", "data after url: $it")
                        _data.emit(it.sortedByDescending { it.date })
                        _loading.emit(false)
                    }
            }
        }
    }
}
package dev.joshhalvorson.data.gallery

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import dev.joshhalvorson.data.AstroFolder
import dev.joshhalvorson.data.DownloadApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryRepository @Inject constructor(private val downloadApi: DownloadApi) {
    private val _allImages = MutableStateFlow(emptyList<AstroData>())

    fun getAllFoldersInStorage(): Flow<List<AstroFolder>> {
        val storageRef = FirebaseStorage.getInstance().reference
        val listRef = storageRef.child("/")

        return listRef.listAllFoldersFlow()
    }

    suspend fun getDataFileForFolders(folders: List<AstroFolder>) = callbackFlow {
        Log.i("getDataFileForFolders", "start")

        val results = folders.map {
            async { getDataFileInFolder(it) }
        }.awaitAll()

        Log.i("getDataFileForFolders", "$results")

        trySend(results.filter { it != AstroData.EMPTY })
        awaitClose { }
    }

    suspend fun getDownloadUrlForImage(data: List<AstroData>) = callbackFlow {
        Log.i("getDownloadUrlForImage", "start")

        val results = data.map { astroData ->
            async {
                val thumbNailUri = astroData.thumbnail.getDownloadUrlFlow().first()
                val fullSizeUri = astroData.fullSize.getDownloadUrlFlow().first()

                astroData.thumbnail = thumbNailUri.toString()
                astroData.fullSize = fullSizeUri.toString()

                astroData
            }
        }.awaitAll()

        Log.i("getDownloadUrlForImage", "$results")
        _allImages.emit(results)

        trySend(results)
        awaitClose { }
    }


    @OptIn(FlowPreview::class)
    private suspend fun getDataFileInFolder(folder: AstroFolder): AstroData {
        val storageRef = folder.reference

        return storageRef.getFirstFileFlow()
            .flatMapConcat {
                if (it == storageRef) {
                    flow { emit(Uri.EMPTY) }
                } else {
                    it.getDownloadUrlFlow()
                }
            }
            .flatMapConcat {
                if (it == Uri.EMPTY) {
                    flow { emit(AstroData.EMPTY) }
                } else {
                    flow { emit(downloadApi.getData(fileUrl = it.toString()).body() ?: return@flow) }
                }
            }
            .first()
    }

    fun getImage(id: String): AstroData {
        return _allImages.value.first { it.id == id }
    }
}

fun StorageReference.listAllFoldersFlow(): Flow<List<AstroFolder>> = callbackFlow {
    listAll()
        .addOnSuccessListener {
            Log.i("listAllFoldersFlow", it.toString())
            trySend(it.prefixes.map { AstroFolder(name = it.name, reference = it) })
        }
        .addOnFailureListener {
            Log.e("listAllFoldersFlow", "error", it)
            cancel()
        }
    awaitClose()
}

fun StorageReference.listAllFilesFlow(): Flow<List<StorageReference>> = callbackFlow {
    listAll()
        .addOnSuccessListener {
            Log.i("listAllFilesFlow", it.toString())
            trySend(it.items)
        }
        .addOnFailureListener {
            Log.e("listAllFilesFlow", "error", it)
            cancel()
        }
    awaitClose()
}

fun StorageReference.getFirstFileFlow(): Flow<StorageReference> = callbackFlow {
    listAll()
        .addOnSuccessListener {
            Log.i("getFirstFileFlow", it.items.toString())
            trySend(it.items.firstOrNull() ?: this@getFirstFileFlow)
        }
        .addOnFailureListener {
            Log.e("getFirstFileFlow", "error", it)
            cancel()
        }
    awaitClose()
}

fun StorageReference.getDownloadUrlFlow(): Flow<Uri> = callbackFlow {
    downloadUrl
        .addOnSuccessListener {
            Log.i("getDownloadUrlFlow", it.toString())
            trySend(it)
        }
        .addOnFailureListener {
            Log.e("getDownloadUrlFlow", "error", it)
            cancel()
        }
    awaitClose()
}

fun String.getDownloadUrlFlow(): Flow<Uri> = callbackFlow {
    FirebaseStorage.getInstance().reference.child(this@getDownloadUrlFlow).downloadUrl
        .addOnSuccessListener {
            Log.i("getDownloadUrlFlow", it.toString())
            trySend(it)
        }
        .addOnFailureListener {
            Log.e("getDownloadUrlFlow", "error", it)
            cancel()
        }
    awaitClose()
}
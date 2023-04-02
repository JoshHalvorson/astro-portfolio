package dev.joshhalvorson.data

import com.google.firebase.storage.StorageReference

data class AstroFolder(
    val name: String,
    val reference: StorageReference
)
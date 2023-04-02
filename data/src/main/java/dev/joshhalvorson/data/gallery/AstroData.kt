package dev.joshhalvorson.data.gallery

data class AstroData(
    val id: String,
    val name: String,
    val type: String,
    val date: String,
    var thumbnail: String,
    var fullSize: String
) {
    companion object {
        val EMPTY = AstroData("", "", "", "", "", "")
    }
}
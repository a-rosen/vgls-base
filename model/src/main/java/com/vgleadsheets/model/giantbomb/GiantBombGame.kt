package com.vgleadsheets.model.giantbomb

data class GiantBombGame(
    val id: Long,
    val name: String,
    val aliases: String?,
    val image: GiantBombImage
) {
    companion object {
        const val ID_NOT_FOUND = -1234L
        const val ID_NO_API = -5678L
    }
}

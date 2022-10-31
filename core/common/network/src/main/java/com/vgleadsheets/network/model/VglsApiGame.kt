package com.vgleadsheets.network.model

@Suppress("ConstructorParameterNaming")
data class VglsApiGame(
    val aliases: List<String>?,
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>,
    val image_url: String?
)

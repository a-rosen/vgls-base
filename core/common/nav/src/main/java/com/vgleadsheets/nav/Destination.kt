package com.vgleadsheets.nav

enum class Destination(
    val destName: String,
    val argType: ArgType,
    val renderAsGrid: Boolean = false,
    val isImplemented: Boolean = true,
) {
    HOME("home", ArgType.NONE),
    BROWSE("browse", ArgType.NONE),
    SEARCH("search", ArgType.NONE, isImplemented = false),
    MENU("menu", ArgType.NONE, isImplemented = false),
    PART_PICKER("parts", ArgType.NONE),
    GAME_DETAIL("games", ArgType.LONG),
    GAMES_LIST("games", ArgType.NONE, renderAsGrid = true),
    COMPOSER_DETAIL("composers", ArgType.LONG),
    COMPOSERS_LIST("composers", ArgType.NONE, renderAsGrid = true),
    SONG_VIEWER("songs/viewer", ArgType.TWO),
    SONG_DETAIL("songs", ArgType.LONG),
    SONGS_LIST("songs", ArgType.NONE),
    TAGS_LIST("tags", ArgType.NONE, isImplemented = false),
    TAGS_VALUES_LIST("tags", ArgType.LONG, isImplemented = false),
    TAGS_VALUES_SONG_LIST("tags/value", ArgType.LONG, isImplemented = false),
    FAVORITES("favorites", ArgType.NONE, isImplemented = false),
    ;

    fun noArgs() = destName
    fun forId(id: Long) = "$destName/$id"
    fun forTwoArgs(id: Long, second: Long) = "$destName/$id/$second"
    fun forString(arg: String) = "$destName/$arg"
    fun template(): String {
        return when (argType) {
            ArgType.NONE -> destName
            ArgType.TWO -> "$destName/{$ARG_TEMPLATE_ONE}/{$ARG_TEMPLATE_TWO}"
            else -> "$destName/{$ARG_TEMPLATE_ONE}"
        }
    }
}

const val ARG_TEMPLATE_ONE = "arg_one"
const val ARG_TEMPLATE_TWO = "arg_two"

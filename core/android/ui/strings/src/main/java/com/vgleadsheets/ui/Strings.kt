package com.vgleadsheets.ui

import com.vgleadsheets.ui.strings.R

@Suppress("CyclomaticComplexMethod", "LongMethod")
fun StringId.id(): Int {
    return when (this) {
        StringId.APP_NAME -> R.string.app_name
        StringId.SCREEN_TITLE_PART_SELECTOR -> R.string.screen_title_parts
        StringId.SCREEN_TITLE_BROWSE -> R.string.screen_title_browse
        StringId.SCREEN_TITLE_BROWSE_GAMES -> R.string.screen_title_games
        StringId.SCREEN_TITLE_BROWSE_COMPOSERS -> R.string.screen_title_composers
        StringId.SCREEN_TITLE_BROWSE_TAGS -> R.string.screen_title_tags
        StringId.SCREEN_TITLE_BROWSE_BY_TAG -> R.string.screen_title_by_tag
        StringId.SCREEN_TITLE_BROWSE_SONGS_WITH_TAG -> R.string.screen_title_songs_with_tag
        StringId.SCREEN_TITLE_BROWSE_ALL -> R.string.screen_title_all
        StringId.SCREEN_TITLE_BROWSE_FAVORITES -> R.string.screen_title_favorites
        StringId.SCREEN_TITLE_SEARCH -> R.string.screen_title_search
        StringId.SCREEN_TITLE_SETTINGS -> R.string.screen_title_settings
        StringId.SCREEN_TITLE_LICENSES -> R.string.screen_title_licenses

        StringId.SCREEN_SUBTITLE_SONG_DETAIL -> R.string.screen_subtitle_song_detail

        StringId.SECTION_HEADER_SEARCH_SONGS -> R.string.section_header_search_songs
        StringId.SECTION_HEADER_SEARCH_GAMES -> R.string.section_header_search_games
        StringId.SECTION_HEADER_SEARCH_COMPOSERS -> R.string.section_header_search_composers
        StringId.SECTION_HEADER_COMPOSERS_FROM_SONG -> R.string.section_header_composers_from_song
        StringId.SECTION_HEADER_COMPOSERS_FROM_GAME -> R.string.section_header_composers_from_game
        StringId.SECTION_HEADER_SONGS_FROM_GAME -> R.string.section_header_songs_from_game
        StringId.SECTION_HEADER_SONGS_FROM_COMPOSER -> R.string.section_header_songs_from_composer
        StringId.SECTION_HEADER_GAMES_FROM_SONG -> R.string.section_header_games_from_song
        StringId.SECTION_HEADER_GAMES_FROM_COMPOSER -> R.string.section_header_games_from_composer
        StringId.SECTION_HEADER_DIFFICULTY_FOR_SONG -> R.string.section_header_difficulty_for_song
        StringId.SECTION_HEADER_ABOUT_SONG -> R.string.section_header_about_song

        StringId.CTA_FAVORITE_ADD -> R.string.cta_favorite_add
        StringId.CTA_FAVORITE_REMOVE -> R.string.cta_favorite_remove

        StringId.LABEL_SONG_ALSO_KNOWN_AS -> R.string.label_song_also_known_as

        StringId.HOME_SECTION_RECENT_SONGS -> R.string.home_section_recent_songs
        StringId.HOME_SECTION_MOST_SONGS_GAMES -> R.string.home_section_most_songs_games
        StringId.HOME_SECTION_MOST_SONGS_COMPOSERS -> R.string.home_section_most_songs_composers
        StringId.HOME_SECTION_MOST_PLAYS_GAMES -> R.string.home_section_most_plays_games
        StringId.HOME_SECTION_MOST_PLAYS_SONGS -> R.string.home_section_most_plays_songs
        StringId.HOME_SECTION_MOST_PLAYS_COMPOSERS -> R.string.home_section_most_plays_composers
        StringId.HOME_SECTION_RNG -> R.string.home_section_rng

        StringId.HOME_ACTION_RANDOM_SONG -> R.string.home_action_random_song
        StringId.HOME_ACTION_RANDOM_GAME -> R.string.home_action_random_game
        StringId.HOME_ACTION_RANDOM_COMPOSER -> R.string.home_action_random_composer

        StringId.BROWSE_LINK_FAVORITES -> R.string.browse_link_favorites
        StringId.BROWSE_LINK_GAME -> R.string.browse_link_game
        StringId.BROWSE_LINK_COMPOSER -> R.string.browse_link_composer
        StringId.BROWSE_LINK_TAG -> R.string.browse_link_tag
        StringId.BROWSE_LINK_SHEETS -> R.string.browse_link_sheets

        StringId.PART_MID_C -> R.string.part_mid_c
        StringId.PART_MID_B -> R.string.part_mid_b
        StringId.PART_MID_E -> R.string.part_mid_e
        StringId.PART_MID_F -> R.string.part_mid_f
        StringId.PART_MID_G -> R.string.part_mid_g
        StringId.PART_MID_ALTO -> R.string.part_mid_alto
        StringId.PART_MID_BASS -> R.string.part_mid_bass
        StringId.PART_MID_VOCAL -> R.string.part_mid_vocal

        StringId.PART_LONG_C -> R.string.part_long_c
        StringId.PART_LONG_B -> R.string.part_long_b
        StringId.PART_LONG_E -> R.string.part_long_e
        StringId.PART_LONG_F -> R.string.part_long_f
        StringId.PART_LONG_G -> R.string.part_long_g
        StringId.PART_LONG_ALTO -> R.string.part_long_alto
        StringId.PART_LONG_BASS -> R.string.part_long_bass
        StringId.PART_LONG_VOCAL -> R.string.part_long_vocal

        StringId.SETTINGS_LABEL_KEEP_SCREEN_ON -> R.string.label_settings_keep_screen_on
        StringId.SETTINGS_LABEL_LICENSES -> R.string.label_settings_license
        StringId.SETTINGS_LABEL_WEBSITE -> R.string.label_settings_website
        StringId.SETTINGS_LABEL_GIANT_BOMB -> R.string.label_settings_gb

        StringId.NOTIF_UPDATE_SUCCESS -> R.string.notif_update_success

        StringId.ERROR_API_UPDATE -> R.string.error_api_update
        StringId.ERROR_DB_UPDATE -> R.string.error_db_update

        StringId.TAG_CAPTION_AND_OTHERS -> R.string.tag_caption_and_others
        StringId.TAG_CAPTION_SEPARATOR -> R.string.tag_caption_separator
    }
}

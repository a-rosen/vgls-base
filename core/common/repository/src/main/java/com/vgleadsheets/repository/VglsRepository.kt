package com.vgleadsheets.repository

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface VglsRepository {
    // Full Lists
    fun getAllSongs(): Flow<List<Song>>
    fun getAllComposers(): Flow<List<Composer>>
    fun getAllTagKeys(): Flow<List<TagKey>>

    // Favorites
    fun getFavoriteSongs(): Flow<List<Song>>

    fun getFavoriteComposers(): Flow<List<Composer>>

    // Related Lists
    fun getSongsForGame(gameId: Long): Flow<List<Song>>
    fun getSongsForGameSync(gameId: Long): List<Song>
    fun getSongsForComposer(composerId: Long): Flow<List<Song>>
    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>
    fun getComposersForSong(songId: Long): Flow<List<Composer>>
    fun getComposersForSongSync(composerId: Long): List<Composer>
    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>
    fun getAliasesForSong(songId: Long): Flow<List<SongAlias>>

    // Single items
    fun getSong(songId: Long): Flow<Song>
    fun getComposer(composerId: Long): Flow<Composer>
    fun getTagKey(tagKeyId: Long): Flow<TagKey>
    fun getTagValue(tagValueId: Long): Flow<TagValue>

    // Etc
    fun searchSongsCombined(searchQuery: String): Flow<List<Song>>
    fun searchComposersCombined(searchQuery: String): Flow<List<Composer>>

    // User data
    suspend fun incrementViewCounter(songId: Long)
    suspend fun toggleFavoriteSong(songId: Long)
    suspend fun toggleFavoriteGame(gameId: Long)
    suspend fun toggleFavoriteComposer(composerId: Long)
    suspend fun toggleOfflineSong(songId: Long)
    suspend fun toggleOfflineGame(gameId: Long)
    suspend fun toggleOfflineComposer(composerId: Long)

    suspend fun toggleAlternate(songId: Long)
}

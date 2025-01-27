package com.vgleadsheets.repository.history

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.database.source.ComposerPlayCountDataSource
import com.vgleadsheets.database.source.GamePlayCountDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.database.source.SongPlayCountDataSource
import com.vgleadsheets.database.source.TagValuePlayCountDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongHistoryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SongHistoryRepository(
    private val songHistoryDataSource: SongHistoryDataSource,
    private val gamePlayCountDataSource: GamePlayCountDataSource,
    private val composerPlayCountDataSource: ComposerPlayCountDataSource,
    private val songPlayCountDataSource: SongPlayCountDataSource,
    private val tagValuePlayCountDataSource: TagValuePlayCountDataSource,
    private val gameDataSource: GameDataSource,
    private val composerDataSource: ComposerDataSource,
    private val tagValueDataSource: TagValueDataSource,
    private val songDataSource: SongDataSource,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) {
    fun recordSongPlay(
        song: Song,
        currentTime: Long = System.currentTimeMillis(),
    ) {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Recording play for song: ${song.gameName} - ${song.name}")

            songPlayCountDataSource.incrementPlayCount(song.id, currentTime)
            gamePlayCountDataSource.incrementPlayCount(song.gameId, currentTime)
            songHistoryDataSource.insert(
                SongHistoryEntry(
                    songId = song.id,
                    timeMs = currentTime
                )
            )

            incrementPlayCountForComposersForSong(song.id, currentTime)
            incrementPlayCountForTagValuesForSong(song.id, currentTime)
        }
    }

    fun recordSongPlayLegacy(
        song: Song,
    ) {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Recording legacy play for song: ${song.gameName} - ${song.name}")

            songDataSource.incrementPlayCount(song.id)
            gameDataSource.incrementSheetsPlayed(song.gameId)
            incrementPlayCountForComposersForSongLegacy(song.id)
        }
    }

    fun getSongPlayCount(songId: Long) = songPlayCountDataSource
        .getPlayCount(songId)

    fun getMostPlaysGames() = gamePlayCountDataSource
        .getMostPlays()
        .mapListTo { item ->
            item to gameDataSource.getOneByIdSync(item.id)
        }

    fun getMostPlaysComposers() = composerPlayCountDataSource
        .getMostPlays()
        .mapListTo { item ->
            item to composerDataSource.getOneByIdSync(item.id)
        }

    fun getMostPlaysTagValues() = tagValuePlayCountDataSource
        .getMostPlays()
        .mapListTo { item ->
            item to tagValueDataSource.getOneByIdSync(item.id)
        }
        .map { list ->
            list.filter { pair -> !pair.second.isDifficultyValue() }
        }

    fun getMostPlaysSongs() = songPlayCountDataSource
        .getMostPlays()
        .mapListTo { item ->
            item to songDataSource.getOneByIdSync(item.id)
        }

    fun getRecentSongs() = songHistoryDataSource
        .getRecentSongs()
        .map { list ->
            list.filter { it.id != null }
                .distinctBy { it.songId }
                .take(MAXIMUM_SONG_HISTORY)
                .map { item -> item to songDataSource.getOneByIdSync(item.songId) }
        }

    private suspend fun incrementPlayCountForComposersForSong(id: Long, currentTime: Long) {
        val composers = composerDataSource.getComposersForSongSync(id)

        composers.forEach {
            composerPlayCountDataSource.incrementPlayCount(it.id, currentTime)
        }
    }

    private suspend fun incrementPlayCountForComposersForSongLegacy(id: Long) {
        val composers = composerDataSource.getComposersForSongSync(id)

        composers.forEach {
            composerDataSource.incrementSheetsPlayed(it.id)
        }
    }

    private suspend fun incrementPlayCountForTagValuesForSong(id: Long, currentTime: Long) {
        val tagValues = tagValueDataSource.getTagValuesForSongSync(id)

        tagValues.forEach {
            tagValuePlayCountDataSource.incrementPlayCount(it.id, currentTime)
        }
    }

    companion object {
        const val MAXIMUM_SONG_HISTORY = 5
    }
}

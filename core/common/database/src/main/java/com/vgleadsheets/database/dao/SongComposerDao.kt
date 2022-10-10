package com.vgleadsheets.database.dao

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongComposerDao {

    suspend fun insertAll(songComposerJoins: List<SongComposerJoin>)

    """
            SELECT * FROM composer INNER JOIN song_composer_join 
            ON composer.id=song_composer_join.composerId
            WHERE song_composer_join.songId=:songId
            ORDER BY name
            COLLATE NOCASE
            """
    )
    fun getComposersForSong(songId: Long): List<Composer>

    """
            SELECT * FROM song INNER JOIN song_composer_join 
            ON song.id=song_composer_join.songId
            WHERE song_composer_join.composerId=:composerId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForComposerSync(composerId: Long): List<Song>

    """
            SELECT * FROM song INNER JOIN song_composer_join 
            ON song.id=song_composer_join.songId
            WHERE song_composer_join.composerId=:composerId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForComposer(composerId: Long): Flow<List<Song>>

    suspend fun nukeTable()
}

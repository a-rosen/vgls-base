package com.vgleadsheets.database.dao

import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface SongTagValueDao {

    suspend fun insertAll(songTagValueJoins: List<SongTagValueJoin>)

    """
            SELECT * FROM tag_value INNER JOIN song_tag_value_join 
            ON tag_value.id=song_tag_value_join.tagValueId
            WHERE song_tag_value_join.songId=:songId
            ORDER BY tag_key_name
            COLLATE NOCASE
            """
    )
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>

    """
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForTagValueSync(tagValueId: Long): List<Song>

    """
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>

    suspend fun nukeTable()
}

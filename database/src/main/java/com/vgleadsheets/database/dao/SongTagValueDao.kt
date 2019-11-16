package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.tag.TagValueEntity
import io.reactivex.Observable

@Dao
interface SongTagValueDao {
    @Insert
    fun insertAll(songTagValueJoins: List<SongTagValueJoin>)

    @Query(
        """ 
            SELECT * FROM tag_value INNER JOIN song_tag_value_join 
            ON tag_value.id=song_tag_value_join.tagValueId
            WHERE song_tag_value_join.songId=:songId
            ORDER BY name
            """
    )
    fun getTagValuesForSong(songId: Long): List<TagValueEntity>

    @Query(
        """ 
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            """
    )
    fun getSongsForTagValueSync(tagValueId: Long): List<SongEntity>

    @Query(
        """ 
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            """
    )
    fun getSongsForTagValue(tagValueId: Long): Observable<List<SongEntity>>

    @Query("DELETE FROM song_tag_value_join")
    fun nukeTable()
}

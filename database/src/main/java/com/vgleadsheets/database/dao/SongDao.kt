package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface SongDao {
    @Query("SELECT * FROM song WHERE game_id = :gameId")
    fun getSongsForGame(gameId: Long): Observable<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :gameId")
    fun getSongsForGameSync(gameId: Long): List<SongEntity>

    @Query("SELECT * FROM song WHERE id = :songId")
    fun getSong(songId: Long): Observable<SongEntity>

    @Insert
    fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM song")
    fun nukeTable()
}

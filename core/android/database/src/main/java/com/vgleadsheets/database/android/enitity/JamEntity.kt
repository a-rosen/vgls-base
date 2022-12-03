package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.JamEntity.Companion.TABLE

@Entity(tableName = TABLE)
data class JamEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val currentSheetId: Long?
) {
    companion object {
        const val TABLE = "jam"
    }
}
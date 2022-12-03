package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Jam
import kotlinx.coroutines.flow.Flow

interface JamDataSource : OneToManyDataSource<Jam> {
    fun searchByName(name: String): Flow<List<Jam>>

    fun remove(id: Long)
}
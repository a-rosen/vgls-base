package com.vgleadsheets.database.dao

import com.vgleadsheets.model.alias.GameAlias
import kotlinx.coroutines.flow.Flow

interface GameAliasDataSource : RegularDataSource<GameAlias> {
    fun searchByName(name: String): Flow<List<GameAlias>>
}
package com.vgleadsheets.database.dao

import com.vgleadsheets.model.alias.ComposerAlias
import kotlinx.coroutines.flow.Flow

interface ComposerAliasDao : RegularDao<ComposerAlias> {
    fun searchByName(name: String): Flow<List<ComposerAlias>>
}

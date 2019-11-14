package com.vgleadsheets.features.main.tag_key

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.tag.TagKey

data class TagKeyState(
    val tags: Async<List<TagKey>> = Uninitialized
) : MvRxState

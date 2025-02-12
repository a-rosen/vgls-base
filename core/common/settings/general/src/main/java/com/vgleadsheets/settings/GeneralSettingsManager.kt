package com.vgleadsheets.settings

import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.flow.map

class GeneralSettingsManager(
    private val storage: Storage
) {
    fun getKeepScreenOn() = getBoolean(SETTING_KEEP_SCREEN_ON, true)

    fun setKeepScreenOn(value: Boolean) = setBoolean(SETTING_KEEP_SCREEN_ON, value)

    fun getNeedsAutoMigrate() = getBoolean(SETTING_NEEDS_AUTO_MIGRATE, true)

    fun setNeedsAutoMigrate(value: Boolean) = setBoolean(SETTING_NEEDS_AUTO_MIGRATE, value)

    private fun setBoolean(key: String, value: Boolean) {
        storage.saveString(key, value.toString())
    }

    private fun getBoolean(key: String, default: Boolean) = storage
        .savedStringFlow(key)
        .map { it?.toBooleanStrictOrNull() ?: default }

    companion object {
        private const val SETTING_KEEP_SCREEN_ON = "setting.general.screen"
        private const val SETTING_NEEDS_AUTO_MIGRATE = "setting.general.automigrate"
    }
}

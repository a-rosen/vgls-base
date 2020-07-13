package com.vgleadsheets.tracking

import android.app.Activity

interface Tracker {
    fun logScreenView(
        activity: Activity,
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logSongView(
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logWebLaunch(
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logMenuShow()

    fun logAutoRefresh()
    fun logForceRefresh()

    fun logSearch(query: String)
    fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String)

    fun logPartSelect(transposition: String)
    fun logRandomSongView(songName: String, gameName: String, transposition: String)

    fun logStickerBr()
    fun logError(message: String)
}

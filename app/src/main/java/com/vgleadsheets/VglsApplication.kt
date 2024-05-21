package com.vgleadsheets

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.airbnb.mvrx.Mavericks
import com.facebook.stetho.Stetho
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.SheetPreviewFetcher
import com.vgleadsheets.logging.Hatchet
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named
import okhttp3.OkHttpClient

@HiltAndroidApp
class VglsApplication :
    Application(),
    ImageLoaderFactory {
    @Inject
    @Named("VglsOkHttp")
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var coilLogger: HatchetCoilLogger

    @Inject
    lateinit var sheetPreviewFetcherFactory: SheetPreviewFetcher.Factory

    override fun onCreate() {
        super.onCreate()

        Mavericks.initialize(this)

        hatchet.v("Starting Application.")
        hatchet.v("Build type: ${BuildConfig.BUILD_TYPE}")

        hatchet.v("App version name: ${BuildConfig.VERSION_NAME}")
        hatchet.v("App version code: ${BuildConfig.VERSION_CODE}")

        hatchet.v("Android version: ${Build.VERSION.RELEASE}")
        hatchet.v("Device manufacturer: ${Build.MANUFACTURER}")
        hatchet.v("Device model: ${Build.MODEL}")

        Stetho.initializeWithDefaults(this)
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(coilLogger)
        .okHttpClient(okHttpClient)
        .components { add(sheetPreviewFetcherFactory) }
        .build()
}

package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.JamConverter
import com.vgleadsheets.conversion.android.converter.SetlistEntryConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryEntryConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.datasource.ComposerAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.DbStatisticsAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.JamAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SetlistEntryAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongHistoryEntryAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagKeyAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagValueAndroidDataSource
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.JamRoomDao
import com.vgleadsheets.database.android.dao.SetlistEntryRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.JamDataSource
import com.vgleadsheets.database.dao.SetlistEntryDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.SongHistoryEntryDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun composerAliasDataSource(
        convert: ComposerAliasConverter,
        roomImpl: ComposerAliasRoomDao,
    ): ComposerAliasDataSource = ComposerAliasAndroidDataSource(
        convert,
        roomImpl
    )

    @Provides
    @Singleton
    fun composerDataSource(
        convert: ComposerConverter,
        manyConverter: SongConverter,
        roomImpl: ComposerRoomDao,
        relatedRoomImpl: SongRoomDao
    ): ComposerDataSource = ComposerAndroidDataSource(
        convert,
        manyConverter,
        roomImpl,
        relatedRoomImpl
    )

    @Provides
    @Singleton
    fun dbstatsDataSource(
        roomImpl: DbStatisticsRoomDao,
    ): DbStatisticsDataSource = DbStatisticsAndroidDataSource(
        roomImpl,
    )

    @Provides
    @Singleton
    fun gameAliasDataSource(
        convert: GameAliasConverter,
        roomImpl: GameAliasRoomDao,
    ): GameAliasDataSource = GameAliasAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun gameDataSource(
        convert: GameConverter,
        manyConverter: SongConverter,
        roomImpl: GameRoomDao,
        relatedRoomImpl: SongRoomDao
    ): GameDataSource = GameAndroidDataSource(
        convert,
        manyConverter,
        roomImpl,
        relatedRoomImpl
    )

    @Provides
    @Singleton
    fun jamDataSource(
        convert: JamConverter,
        otoRelatedRoomImpl: SongRoomDao,
        otmRelatedRoomImpl: SongHistoryEntryRoomDao,
        roomImpl: JamRoomDao,
        songConverter: SongConverter,
        songHistoryEntryConverter: SongHistoryEntryConverter
    ): JamDataSource = JamAndroidDataSource(
        convert,
        roomImpl,
        otoRelatedRoomImpl,
        otmRelatedRoomImpl,
        songConverter,
        songHistoryEntryConverter
    )

    @Provides
    @Singleton
    fun setlistEntryDataSource(
        convert: SetlistEntryConverter,
        foreignConverter: SongConverter,
        roomImpl: SetlistEntryRoomDao,
        relatedRoomImpl: SongRoomDao
    ): SetlistEntryDataSource = SetlistEntryAndroidDataSource(
        convert,
        foreignConverter,
        roomImpl,
        relatedRoomImpl
    )

    @Provides
    @Singleton
    fun songDataSource(
        convert: SongConverter,
        manyConverter: ComposerConverter,
        tagValueConverter: TagValueConverter,
        roomImpl: SongRoomDao,
        relatedRoomImpl: ComposerRoomDao,
        tagValueRoomImpl: TagValueRoomDao
    ): SongDataSource = SongAndroidDataSource(
        convert,
        manyConverter,
        tagValueConverter,
        roomImpl,
        relatedRoomImpl,
        tagValueRoomImpl
    )

    @Provides
    @Singleton
    fun songHistoryEntryDataSource(
        convert: SongHistoryEntryConverter,
        foreignConvert: SongConverter,
        roomImpl: SongHistoryEntryRoomDao,
        relatedRoomImpl: SongRoomDao
    ): SongHistoryEntryDataSource = SongHistoryEntryAndroidDataSource(
        convert,
        foreignConvert,
        roomImpl,
        relatedRoomImpl
    )

    @Provides
    @Singleton
    fun tagKeyDataSource(
        convert: TagKeyConverter,
        manyConverter: TagValueConverter,
        roomImpl: TagKeyRoomDao,
        relatedRoomImpl: TagValueRoomDao
    ): TagKeyDataSource = TagKeyAndroidDataSource(
        convert,
        manyConverter,
        roomImpl,
        relatedRoomImpl
    )

    @Provides
    @Singleton
    fun tagValueDataSource(
        convert: TagValueConverter,
        manyConverter: SongConverter,
        roomImpl: TagValueRoomDao,
        relatedRoomImpl: SongRoomDao
    ): TagValueDataSource = TagValueAndroidDataSource(
        convert,
        manyConverter,
        roomImpl,
        relatedRoomImpl
    )
}

package com.trucksup.field_officer.data.di

import android.content.Context
import androidx.room.Room
import com.trucksup.field_officer.data.database.TrucksubFODB
import com.trucksup.field_officer.data.database.dao.TrucksubFODAO
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.data.repository.impl.APIRepositoryImpl
import com.trucksup.field_officer.data.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAPIRepository(apiService: ApiService): APIRepository {
        return APIRepositoryImpl(apiService);
    }

    @Provides
    @Singleton
    fun provideTranslationDAO(saloonDB: TrucksubFODB): TrucksubFODAO {
        return saloonDB.getTrucksubFODao()
    }

    @Singleton
    @Provides
    fun provideTrucksubFODB(@ApplicationContext app: Context): TrucksubFODB {
        val dbBuilder = Room.databaseBuilder(app,
            TrucksubFODB::class.java,
            "trucksupfo_db"
        )
        return dbBuilder.build() // The reason we can construct a database for the repo
    }
}
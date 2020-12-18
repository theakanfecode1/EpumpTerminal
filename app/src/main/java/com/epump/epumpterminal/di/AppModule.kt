package com.epump.epumpterminal.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.epump.epumpterminal.api.*
import com.epump.epumpterminal.db.EpumpDatabase
import com.epump.epumpterminal.db.PumpDao
import com.epump.epumpterminal.db.UserDao
import com.epump.epumpterminal.repository.LoginRepository
import com.epump.epumpterminal.repository.PumpRepository
import com.epump.epumpterminal.repository.StationRepository
import com.epump.epumpterminal.repository.TankRepository
import com.epump.epumpterminal.ui.SplashScreenFragment
import com.epump.epumpterminal.utils.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient.addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }


        return okHttpClient.build()

    }

    @Singleton
    @Provides
    fun providerRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    fun provideEpumpService(retrofit: Retrofit): EpumpService =
        retrofit.create(EpumpService::class.java)

    @Singleton
    @Provides
    fun provideLoginDataSource(epumpService: EpumpService) = LoginDataSource(epumpService)

    @Singleton
    @Provides
    fun provideStationDataSource(epumpService: EpumpService) = StationDataSource(epumpService)

    @Singleton
    @Provides
    fun providePumpDataSource(epumpService: EpumpService) = PumpsDataSource(epumpService)

    @Singleton
    @Provides
    fun provideTankDataSource(epumpService: EpumpService) = TankDataSource(epumpService)

    @Singleton
    @Provides
    fun providePumpRepository(pumpsDataSource: PumpsDataSource, pumpDao: PumpDao) =
        PumpRepository(pumpsDataSource, pumpDao)

    @Singleton
    @Provides
    fun provideTankRepository(tankDataSource: TankDataSource) = TankRepository(tankDataSource)


    @Singleton
    @Provides
    fun provideLoginRepository(loginDataSource: LoginDataSource, userDao: UserDao) =
        LoginRepository(loginDataSource, userDao)

    @Singleton
    @Provides
    fun provideStationRepository(stationDataSource: StationDataSource) =
        StationRepository(stationDataSource)

    @Singleton
    @Provides
    fun provideEpumpDB(@ApplicationContext context: Context): EpumpDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EpumpDatabase::class.java,
            "epump_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(epumpDatabase: EpumpDatabase): UserDao {
        return epumpDatabase.userDao()
    }

    @Singleton
    @Provides
    fun providePumpDao(epumpDatabase: EpumpDatabase): PumpDao {
        return epumpDatabase.pumpDao()
    }


}
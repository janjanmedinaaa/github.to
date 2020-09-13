package com.medina.juanantonio.githubto.di

import android.content.Context
import androidx.room.Room
import com.medina.juanantonio.githubto.data.database.GithubToDb
import com.medina.juanantonio.githubto.data.manager.DatabaseManager
import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.network.ApiService
import com.medina.juanantonio.githubto.network.INetworkManager
import com.medina.juanantonio.githubto.network.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkManager(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): INetworkManager {
        return NetworkManager(context, apiService)
    }

    @Provides
    @Singleton
    fun provideFITGuardDb(@ApplicationContext context: Context): GithubToDb {
        return Room.databaseBuilder(context, GithubToDb::class.java, "githubto.db")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDatabaseManager(githubToDb: GithubToDb): IDatabaseManager {
        return DatabaseManager(githubToDb)
    }
}
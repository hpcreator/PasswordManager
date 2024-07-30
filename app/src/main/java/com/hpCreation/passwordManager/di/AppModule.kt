package com.hpCreation.passwordManager.di

import android.content.Context
import androidx.room.Room
import com.hpCreation.passwordManager.data.PasswordDao
import com.hpCreation.passwordManager.data.PasswordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideDao(passwordDatabase: PasswordDatabase): PasswordDao = passwordDatabase.passwordDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PasswordDatabase =
        Room.databaseBuilder(
            context, PasswordDatabase::class.java, "PasswordDatabase"
        ).fallbackToDestructiveMigration(false).build()
}
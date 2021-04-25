package com.remlexworld.branchapp.di

import android.content.Context
import com.remlexworld.branchapp.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object SessionManagerModule {

    @Provides
    fun provideSessionManager(@ApplicationContext appContext: Context): SessionManager {
        return SessionManager(appContext);
    }

}
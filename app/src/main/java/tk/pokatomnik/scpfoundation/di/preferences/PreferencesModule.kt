package tk.pokatomnik.scpfoundation.di.preferences

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class PreferencesModule {
    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): PreferencesContainer {
        return PreferencesContainer(context)
    }
}
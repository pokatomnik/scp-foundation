package tk.pokatomnik.scpfoundation.di.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HttpModule {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient()
    }
}
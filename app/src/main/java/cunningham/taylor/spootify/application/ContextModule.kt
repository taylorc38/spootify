package cunningham.taylor.spootify.application

import android.content.Context
import android.support.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(@NonNull private val applicationContext: Context) {
    @Provides
    @NonNull
    fun provideApplicationContext() : Context = applicationContext
}
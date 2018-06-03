package cunningham.taylor.spootify.player

import android.content.Context
import cunningham.taylor.spootify.player.local.LocalAudioPlayer
import dagger.Module
import dagger.Provides

@Module
class AudioPlayerModule {
    @Provides
    fun getAudioPlayer(applicationContext: Context) : AudioPlayer {
        return LocalAudioPlayer(applicationContext)
    }
}
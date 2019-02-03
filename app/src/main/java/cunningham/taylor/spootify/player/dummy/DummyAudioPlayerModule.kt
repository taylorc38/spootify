package cunningham.taylor.spootify.player.dummy

import android.support.annotation.NonNull
import cunningham.taylor.spootify.player.AudioPlayer
import dagger.Module
import dagger.Provides

@Module
class DummyAudioPlayerModule {
    @Provides
    fun audioPlayer(@NonNull audioPlayer: DummyAudioPlayer) : AudioPlayer
            = audioPlayer
}
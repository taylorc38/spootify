package cunningham.taylor.spootify.player.local

import cunningham.taylor.spootify.player.AudioPlayer
import dagger.Module
import dagger.Provides

@Module
class LocalAudioPlayerModule {
    @Provides
    fun audioPlayer(audioPlayer: AudioPlayer) : AudioPlayer
            = audioPlayer
}
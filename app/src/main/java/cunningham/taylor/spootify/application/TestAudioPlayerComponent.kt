package cunningham.taylor.spootify.application

import cunningham.taylor.spootify.player.dummy.DummyAudioPlayer
import cunningham.taylor.spootify.player.dummy.DummyAudioPlayerModule
import dagger.Component

@Component(modules = [DummyAudioPlayerModule::class])
interface TestAudioPlayerComponent {
    fun audioPlayer() : DummyAudioPlayer
}
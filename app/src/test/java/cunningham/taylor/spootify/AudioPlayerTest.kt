package cunningham.taylor.spootify

import android.util.Log
import cunningham.taylor.spootify.application.DaggerTestAudioPlayerComponent
import cunningham.taylor.spootify.player.AudioPlayer
import cunningham.taylor.spootify.player.Track
import cunningham.taylor.spootify.player.dummy.DummyAudioPlayer
import cunningham.taylor.spootify.player.dummy.DummyAudioPlayerModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import javax.inject.Inject

// TODO this doesn't work with Kotlin apparently, find another way to prevent errors while using android.util.Log methods

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
class AudioPlayerTest {

    @Inject
    private lateinit var audioPlayer: DummyAudioPlayer

    @Before
    fun setup() {
        val audioPlayerComponent = DaggerTestAudioPlayerComponent.builder()
                .dummyAudioPlayerModule(DummyAudioPlayerModule())
                .build()
        audioPlayer = audioPlayerComponent.audioPlayer()

        audioPlayer.playlist.add(SAMPLE_TRACK_1)
        audioPlayer.playlist.add(SAMPLE_TRACK_2)
        audioPlayer.playlist.add(SAMPLE_TRACK_3)
    }

    @Test
    fun play() {
        audioPlayer.playAtIndex(0)
        assert(audioPlayer.playState == AudioPlayer.PlayState.PLAYING)
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_1)
    }

    @Test
    fun pause() {
        audioPlayer.pause()
        assert(audioPlayer.playState == AudioPlayer.PlayState.PAUSED)
    }

    @Test
    fun resume() {
        audioPlayer.resume()
        assert(audioPlayer.playState == AudioPlayer.PlayState.PLAYING)
    }

    @Test
    fun nextWhilePlaying() {
        audioPlayer.next()
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_2)
        assert(audioPlayer.playState == AudioPlayer.PlayState.PLAYING)
    }

    @Test
    fun nextWhilePaused() {
        audioPlayer.pause()
        audioPlayer.next()
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_3)
        assert(audioPlayer.playState == AudioPlayer.PlayState.PAUSED)
    }

    @Test
    fun previousWhilePaused() {
        audioPlayer.previous()
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_2)
        assert(audioPlayer.playState == AudioPlayer.PlayState.PAUSED)
    }

    @Test
    fun previousWhilePlaying() {
        audioPlayer.resume()
        audioPlayer.previous()
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_1)
        assert(audioPlayer.playState == AudioPlayer.PlayState.PLAYING)
    }

    @Test
    fun nextAtEndOfPlaylist() {
        audioPlayer.playAtIndex(audioPlayer.playlist.size)
        audioPlayer.next()
        assert(audioPlayer.currentTrack == SAMPLE_TRACK_1)
        assert(audioPlayer.playState == AudioPlayer.PlayState.COMPLETE)
    }

    @Test
    fun stop() {
        audioPlayer.playAtIndex(0)
        audioPlayer.stop()
        assert(audioPlayer.playState == AudioPlayer.PlayState.UNINITIALIZED)
    }

    companion object {
        val SAMPLE_TRACK_1 = Track("Sample Track 1", null)
        val SAMPLE_TRACK_2 = Track("Sample Track 2", null)
        val SAMPLE_TRACK_3 = Track("Sample Track 3", null)
    }
}
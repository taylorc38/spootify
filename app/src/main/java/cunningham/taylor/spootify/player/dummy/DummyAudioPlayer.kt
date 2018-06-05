package cunningham.taylor.spootify.player.dummy

import android.os.Handler
import android.util.Log
import cunningham.taylor.spootify.player.AudioPlayer
import cunningham.taylor.spootify.player.Track

class DummyAudioPlayer : AudioPlayer() {

    override var shuffle = false
    override var autoPlay = true
    override var currentTrack: Track? = null
        private set
    private var currentIndex: Int = 0
        private set(value) {
            try {
                field = value
                currentTrack = playlist[value]
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, e.message)
            }
        }
    private val mediaCompletedHandler = Handler()

    private val mediaCompletedRunnable = Runnable {
        Log.i(TAG, "Media complete")
        if (autoPlay) {
            next()
        }
    }

    override fun playAtIndex(index: Int) {
        Log.i(TAG, "playAtIndex($index)")
        playState = PlayState.UNINITIALIZED
        currentIndex = index
        playState = PlayState.PLAYING
        mediaCompletedHandler.postDelayed(mediaCompletedRunnable, TRACK_DURATION)
    }

    override fun next() {
        Log.i(TAG, "next()")
        if (currentIndex < playlist.size - 1) {
            playAtIndex(currentIndex++)
        } else {
            currentIndex = 0
            playState = PlayState.COMPLETE
        }
    }

    override fun previous() {
        Log.i(TAG, "previous()")
        if (currentIndex > 0) {
            playAtIndex(currentIndex--)
        } else {
            playAtIndex(currentIndex)
        }
    }

    override fun pause() {
        Log.i(TAG, "pause()")
        playState = PlayState.PAUSED
    }

    override fun resume() {
        Log.i(TAG, "resume()")
        playState = PlayState.PLAYING
    }

    override fun rewind(milliseconds: Int) {
        Log.i(TAG, "rewind()")
        val seekCachedPlayState = playState
        playState = PlayState.SEEKING
        playState = seekCachedPlayState
    }

    override fun ff(milliseconds: Int) {
        val seekCachedPlayState = playState
        playState = PlayState.SEEKING
        playState = seekCachedPlayState
    }

    override fun stop() {
        playState = PlayState.UNINITIALIZED
    }

    companion object {
        const val TAG = "DummyAudioPlayer"
        const val TRACK_DURATION: Long = 10000 // 10 seconds
    }
}
package cunningham.taylor.spootify.player.local

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import cunningham.taylor.spootify.player.AudioPlayer
import cunningham.taylor.spootify.player.Track

class LocalAudioPlayer(private val applicationContext: Context)
    : AudioPlayer(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {

    override var shuffle = true // TODO implement shuffle
    override var autoPlay = true
    override var currentTrack: Track? = null
        private set
    private var currentIndex = 0
        private set(value) {
            try {
                currentTrack = playlist[value]
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, e.message)
            }
        }
    private val player = MediaPlayer()
    private var seekCachedPlayState: PlayState? = null

    init {
        // MediaPlayer initialization
        player.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
        )
        player.setOnPreparedListener(this)
        player.setOnErrorListener(this)
        player.setOnCompletionListener(this)
        player.setOnSeekCompleteListener(this)
    }

    override fun playAtIndex(index: Int) {
        player.reset()
        currentIndex = index
        if (currentTrack != null && currentTrack?.rawId != null) {
            val assetFileDescriptor: AssetFileDescriptor? = applicationContext.resources.openRawResourceFd(currentTrack!!.rawId!!)
            if (assetFileDescriptor != null) {
                player.setDataSource(assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length)
                assetFileDescriptor.close()
                player.prepareAsync()
                return
            }
            Log.w(TAG, "Problem playing track at index $index: AssetFileDescriptor is null")
            return
        }
        Log.w(TAG, "Problem playing track at index $index: Invalid index")
    }

    override fun next() {
        if (currentIndex < playlist.size - 1) {
            playAtIndex(currentIndex + 1)
        } else {
            currentIndex = 0
            playState = PlayState.COMPLETE
        }
    }

    override fun previous() {
        if (currentIndex > 0
                && (player.isPlaying && player.currentPosition < REPLAY_THRESHOLD_MILLISEC)) {
            playAtIndex(currentIndex - 1)
        } else {
            playAtIndex(currentIndex)
        }
    }

    override fun pause() {
        try {
            player.pause()
            playState = PlayState.PAUSED
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message)
        }
    }

    override fun resume() {
        try {
            player.start()
            playState = PlayState.PLAYING
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message)
        }
    }

    override fun rewind(milliseconds: Int) {
        try {
            val newPosition = player.currentPosition - milliseconds
            if (newPosition > 0) {
                seekCachedPlayState = playState
                playState = PlayState.SEEKING
                player.seekTo(newPosition)
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message)
        }
    }

    override fun ff(milliseconds: Int) {
        try {
            val newPosition = player.currentPosition + milliseconds
            if (newPosition < player.duration) {
                seekCachedPlayState = playState
                playState = PlayState.SEEKING
                player.seekTo(newPosition)
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message)
        }
    }

    override fun stop() {
        try {
            player.stop()
            player.release()
            playState = PlayState.UNINITIALIZED
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message)
        }
    }

    /** MediaPlayer callbacks */

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
        playState = PlayState.PLAYING
    }

    override fun onError(mp: MediaPlayer?, p1: Int, p2: Int): Boolean {
        playState = PlayState.ERROR
        return false
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        if (seekCachedPlayState == PlayState.PLAYING) {
            resume()
        } else {
            playState = seekCachedPlayState!!
            seekCachedPlayState = null
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (autoPlay && currentIndex < playlist.size - 1) {
            currentIndex++
            playAtIndex(currentIndex)
        } else {
            playState = PlayState.COMPLETE
        }
    }

    companion object {
        private const val TAG = "LocalAudioPlayer"
        private const val REPLAY_THRESHOLD_MILLISEC = 5000 // 5 seconds
    }
}
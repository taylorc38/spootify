package cunningham.taylor.spootify.player

import kotlin.properties.Delegates

abstract class AudioPlayer<Track> {

    abstract var shuffle: Boolean
    abstract var autoPlay: Boolean
    abstract val playlist: ArrayList<Track>
    abstract val currentTrack: Track?
    private val listeners: ArrayList<PlayStateListener> = ArrayList()
    var playState: PlayState by Delegates.observable(
            initialValue = PlayState.UNINITIALIZED,
            onChange = {
                _, _, new ->
                for (listener: PlayStateListener in listeners) {
                    listener.onPlayStateChanged(new)
                }
            }
    )

    abstract fun playAtIndex(index: Int)
    abstract fun next()
    abstract fun previous()
    abstract fun pause()
    abstract fun resume()
    abstract fun rewind(milliseconds: Int)
    abstract fun ff(milliseconds: Int)
    abstract fun stop()

    fun addListener(listener: PlayStateListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: PlayStateListener) {
        listeners.remove(listener)
    }

    enum class PlayState {
        UNINITIALIZED,
        PREPARING,
        PLAYING,
        PAUSED,
        SEEKING,
        ERROR,
        COMPLETE
    }

    interface PlayStateListener {
        fun onPlayStateChanged(playState: AudioPlayer.PlayState)
    }
}
package cunningham.taylor.spootify

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import cunningham.taylor.spootify.player.AudioPlayer
import cunningham.taylor.spootify.player.local.LocalAudioPlayer

class MusicService : Service() {

    private val musicBind: IBinder = MusicServiceBinder()
    lateinit var audioPlayer: AudioPlayer

    override fun onCreate() {
        super.onCreate()
        audioPlayer = LocalAudioPlayer(applicationContext)
    }

    override fun onBind(intent: Intent) : IBinder? {
        return musicBind
    }

    override fun onUnbind(intent: Intent?): Boolean {
        super.onUnbind(intent)
        return false
    }

    inner class MusicServiceBinder : Binder() {
        fun getService() : MusicService {
            return this@MusicService
        }
    }
}
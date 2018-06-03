package cunningham.taylor.spootify

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import cunningham.taylor.spootify.application.Injector
import cunningham.taylor.spootify.player.AudioPlayer
import cunningham.taylor.spootify.player.Track
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var musicService: MusicService? = null
    private var playIntent: Intent? = null
    private var musicBound = false
    private val playStateListener: AudioPlayer.PlayStateListener = (object : AudioPlayer.PlayStateListener {
        override fun onPlayStateChanged(playState: AudioPlayer.PlayState) {
            when (playState) {
                AudioPlayer.PlayState.PLAYING -> Injector.get().picasso().load(android.R.drawable.ic_media_pause).into(btnPlay)
                else -> Injector.get().picasso().load(android.R.drawable.ic_media_play).into(btnPlay)
            }
        }
    })

    private val serviceConnection = (object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            musicService = binder.getService()
            musicBound = true

            // Register play state observer
            musicService?.audioPlayer?.addListener(playStateListener)

            // Add a song
            musicService?.audioPlayer?.playlist?.add(
                    Track("Redbone", R.raw.redbone)
            )
            musicService?.audioPlayer?.playlist?.add(
                    Track("Let's Get Scwhify", R.raw.schwifty)
            )
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicBound = false
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        if (playIntent == null) {
            playIntent = Intent(this, MusicService::class.java)
            bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE)
            startService(playIntent)
        }

        btnPlay.setOnClickListener {
            when (musicService?.audioPlayer?.playState) {
                AudioPlayer.PlayState.PLAYING -> musicService?.audioPlayer?.pause()
                AudioPlayer.PlayState.PAUSED -> musicService?.audioPlayer?.resume()
                AudioPlayer.PlayState.UNINITIALIZED -> musicService?.audioPlayer?.playAtIndex(0)
                else -> {}
            }
        }

        btnPrevious.setOnClickListener {
            musicService?.audioPlayer?.previous()
        }

        btnNext.setOnClickListener {
            musicService?.audioPlayer?.next()
        }

        btnRewind.setOnClickListener {
            musicService?.audioPlayer?.rewind(15000)
        }

        btnFf.setOnClickListener {
            musicService?.audioPlayer?.ff(15000)
        }
    }

    override fun onDestroy() {
        stopService(playIntent)
        musicService = null
        super.onDestroy()
    }
}

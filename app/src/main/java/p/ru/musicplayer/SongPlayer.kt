package p.ru.musicplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_song_player.*
import kotlinx.android.synthetic.main.content_song_player.*
import p.ru.musicplayer.utility.AudioPlayer
import java.io.File

class SongPlayer(var player: AudioPlayer) : AppCompatActivity() {

    var song_path : String = Environment.getExternalStorageDirectory().absolutePath

    var song = Song()

    constructor() : this(null!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_player)
//        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        var bundle : Bundle = intent.extras!!
        song_path = bundle.getString("song_path")!!

        Log.d("playing-from", "--> $song_path")
        load(song_path)

        stop_button_song_player.setOnClickListener{
            player.pause()
        }
        play_button_song_player.setOnClickListener{
            player.start()
        }

        pause_button_song_player.setOnClickListener{
            player.pause()
        }

    }

    fun load(song_path : String){
        var f : File = File(song_path)
        var mmt = MediaMetadataRetriever()
        mmt.setDataSource(song_path)
        song.song_name = f.name
        song.song_path = song_path

        song.artist = mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        song.album = mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        song.duration = (mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).toInt() / 1000
        var data = mmt.embeddedPicture
        var img : Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        findViewById<ImageView>(R.id.thumbnail_song_player).setImageBitmap(Bitmap.createScaledBitmap(img, 400, 400, false))

        findViewById<TextView>(R.id.song_name_song_player).setText(song.song_name)
        player.set_path(song_path)
        player.load()
    }

}

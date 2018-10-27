package p.ru.musicplayer

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_song_player.*
import kotlinx.android.synthetic.main.content_song_player.*
import p.ru.musicplayer.utility.AudioPlayer
import p.ru.musicplayer.utility.PrepareData
import p.ru.musicplayer.utility.SeekAnimate
import java.io.File

class SongPlayer(var sk : SeekAnimate?) : AppCompatActivity() {

    var song_path : String = Environment.getExternalStorageDirectory().absolutePath

    var song = Song()

    var player : AudioPlayer = AudioPlayer()

    constructor() : this(null)

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

        player.set_song_list(bundle.getStringArrayList("song_list"))



        Log.d("playing-from", "--> $song_path")
        load(song_path)

        pause_button.setOnClickListener{
            player.pauser()
            if(player.notplaying){
                findViewById<ImageView>(R.id.pause_button).setImageResource(R.drawable.play)
            }else  findViewById<ImageView>(R.id.pause_button).setImageResource(R.drawable.pause)
        }

        prev.setOnClickListener{
            player.change()
            load(player.prev())
        }

        next.setOnClickListener{
            player.change()
            load(player.next())
        }
    }

    fun load(song_path : String){
        val f = File(song_path)
        val mmt = MediaMetadataRetriever()
        mmt.setDataSource(song_path)

        song.song_name = f.name
        song.song_path = song_path
        song.artist = mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        song.album = mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        song.duration = (mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).toInt() / 1000


        val data = mmt.embeddedPicture
        val img : Bitmap
        if(data != null) img  = BitmapFactory.decodeByteArray(data, 0, data.size)
        else img = PrepareData.text_as_bitmap("\u090b", 80F, 0x00e0ff)

        findViewById<ImageView>(R.id.thumbnail_song_player).setImageBitmap(Bitmap.createScaledBitmap(img, 400, 400, false))
        findViewById<TextView>(R.id.song_name_song_player).setText(song.song_name)
        findViewById<SeekBar>(R.id.seek_to_postion).max = song.duration
        findViewById<TextView>(R.id.artist_name_song_player).setText(song.artist)

        animate_seek_bar(0)

        if(sk != null && player.change){
            (sk as SeekAnimate).on_destroy()
            sk = null
            player.change()
        }
        sk  = SeekAnimate(findViewById<SeekBar>(R.id.seek_to_postion), player)
        player.set_song(song)
        player.set_path(song_path)
        player.load(this)
    }

    /**
     * @param max_len max len of seek bar(song) in seconds
     */
    fun animate_seek_bar(max_len : Int){
        var animate = ObjectAnimator.ofInt(findViewById<SeekBar>(R.id.seek_to_postion), "song_seeker", 0, song.duration)
        animate.duration = song.duration.toLong()
        animate.interpolator = DecelerateInterpolator()
        animate.start()
        findViewById<SeekBar>(R.id.seek_to_postion)
    }

}

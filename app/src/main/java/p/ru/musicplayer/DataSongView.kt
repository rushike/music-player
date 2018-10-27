package p.ru.musicplayer

import android.graphics.Bitmap

class DataSongView(val file_path : String, val song_name : String, val artist_name : String?, val thumbnail : Bitmap?) {
    override fun toString(): String {
        return "Song Name : $song_name == Song Path : $file_path"
    }
}
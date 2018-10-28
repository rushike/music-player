package p.ru.musicplayer.models

class Song(var song_name : String? = null, var song_path : String? = null, var artist : String? = null, var album : String? = null, var duration : Int = 0, var current_position : Int = 0, var favourite : Boolean = false, var loop : Boolean) {

    constructor() : this(null, null,null,null,0,0,false, false) {
//        song_name = null
//        song_path = null
//        artist  = null
//        album  = null
//        duration  = 0.0f
//        current_position  = 0.0f
//        favourite = false
//        loop = false
    }
    override fun toString(): String {
        return "Song Name : $song_name"
    }

}
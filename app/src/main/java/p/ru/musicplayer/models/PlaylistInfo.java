package p.ru.musicplayer.models;

public class PlaylistInfo{

    public static final String TABLE_NAME = "playlist_info";

    public static final String PLAYLIST_NAME = "playlist_name";

    public static final String COUNT = "count";

    public static final String TIMESTAMP = "timestamp";

    private String playlist_name;

    public  int count;

    private String timestamp;


    public static String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME  + "( " +
            PLAYLIST_NAME  + " TEXT NOT NULL, " +TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP) ";

    public static String DROP_TABLE = "drop table IF EXISTS " + TABLE_NAME;

    public PlaylistInfo(String playlist_name, int count, String timestamp){
        this.timestamp = timestamp;
        this.count = count;
        this.playlist_name = playlist_name;
    }

    public  String get_playlist_name(){return playlist_name;}
    public String get_timestamp(){return timestamp;}

    public void  set_song_id(String playlist_name){this.playlist_name = playlist_name;}
    public void  set_timestamp(String timestamp){this.timestamp = timestamp;}


    public  String toString(){
        return  "song_id : " + playlist_name + ", timestamp : " + timestamp +"\n";
    }
}

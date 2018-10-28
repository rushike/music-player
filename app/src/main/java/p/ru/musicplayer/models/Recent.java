package p.ru.musicplayer.models;

public class Recent{

    public static final String TABLE_NAME = "recent";

    public static final String SONG_ID = "song_id";

    public static final String TIMESTAMP = "timestamp";

    private String song_id;

    private String timestamp;


    public static String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME  + "( " +
            SONG_ID  + " TEXT NOT NULL, " +TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP) ";

    public static String DROP_TABLE = "drop table IF EXISTS " + TABLE_NAME;

    public Recent(String song_id, String timestamp){
        this.timestamp = timestamp;
        this.song_id = song_id;
    }

    public  String get_song_id(){return song_id;}
    public String get_timestamp(){return timestamp;}

    public void  set_song_id(String song_id){this.song_id = song_id;}
    public void  set_timestamp(String timestamp){this.timestamp = timestamp;}


    public  String toString(){
        return  "song_id : " + song_id + ", timestamp : " + timestamp +"\n";
    }
}

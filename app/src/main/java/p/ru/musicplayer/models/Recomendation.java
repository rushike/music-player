package p.ru.musicplayer.models;

public class Recomendation {

    public static final String TABLE_NAME = "recomendation";

    public static final String SONG_ID = "song_id";

    public static final String TIMESTAMP = "timestamp";

    public static final String COUNT = "count";

    public static final String RVALUE = "rvalue";

    private String song_id;

    private String timestamp;

    private int count;

    private float rvalue;


    public static String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME  + "( " +
                    SONG_ID  + " TEXT NOT NULL, " + COUNT  + " INT NOT NULL, " + RVALUE + " REAL NOT NULL, "
                    + TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP) ";

    public static String DROP_TABLE = "drop table IF EXISTS " + TABLE_NAME;

    public Recomendation(String song_id, int count, float rvalue, String timestamp){
        this.timestamp = timestamp;
        this.song_id = song_id;
        this.rvalue = rvalue;
        this.count = count;
    }

    public  String get_song_id(){return song_id;}
    public String get_timestamp(){return timestamp;}
    public int get_count(){return count;}
    public float get_rvalue(){return rvalue;}

    public void  set_song_id(String song_id){this.song_id = song_id;}
    public void  set_timestamp(String timestamp){this.timestamp = timestamp;}
    public void set_count(int count){this.count = count;}
    public void set_rvalue(float rvalue){this.rvalue = rvalue;}

    public  String toString(){
        return  "song_id : " + song_id + ", count : " + count + ", rvalue : " + rvalue + ", timestamp : " + timestamp +"\n";
    }
}

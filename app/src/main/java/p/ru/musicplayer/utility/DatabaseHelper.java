package p.ru.musicplayer.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import p.ru.musicplayer.models.Favourite;
import p.ru.musicplayer.models.PlaylistInfo;
import p.ru.musicplayer.models.Recent;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;

    public static final  String DATABASE_NAME = "ru_music_player";

    public static final int VERSION_NUMBER = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Favourite.CREATE_TABLE);
        db.execSQL(Recent.CREATE_TABLE);
        db.execSQL(PlaylistInfo.CREATE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert_favourite(String song_id){
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Favourite.SONG_ID, song_id);
        long id = database.insert(Favourite.TABLE_NAME, null, values);

        database.close();
        return id;
    }

    public ArrayList<Favourite> get_all_favourite_songs(){
        ArrayList<Favourite> list = new ArrayList<>();
        database = this.getReadableDatabase();

        String query = "select * from " + Favourite.TABLE_NAME;

        Cursor itr = database.rawQuery(query, null);

        if(itr.moveToFirst()){
            do{
                Favourite fav = new Favourite(itr.getString(itr.getColumnIndex(Favourite.SONG_ID)),
                                    itr.getString(itr.getColumnIndex(Favourite.TIMESTAMP)));
                list.add(fav);
            }while (itr.moveToNext());
        }
        itr.close();
        database.close();

        return list;
    }

    public int get_fav_songs_count() {
        String countQuery = "SELECT  * FROM " + Favourite.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean contains_in_fav(String song_path){
        database = this.getReadableDatabase();
        Cursor qcursor = database.query(Favourite.TABLE_NAME, new String[]{Favourite.SONG_ID}, Favourite.SONG_ID + "=?",
                            new String[]{song_path}, null, null, null, null);

        String id = "-1";
        try {
            if (qcursor != null) {
                qcursor.moveToFirst();
                id = qcursor.getString(qcursor.getColumnIndex(Favourite.SONG_ID));
                qcursor.close();
            }
        }catch (Exception e){
            Log.d("id_to_song", "id_to_song: " + e.toString());
            id = "-1";
        }
        return !id.equals("-1");
    }

    public void delete_favourite_song_by_id(String fav_song_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Favourite.TABLE_NAME, Favourite.SONG_ID + " = ?",
                new String[]{fav_song_id});
        db.close();
    }



    public long insert_recent(String song_id){
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Recent.SONG_ID, song_id);
        long id = database.insert(Recent.TABLE_NAME, null, values);

        database.close();
        return id;
    }

    public ArrayList<Recent> get_all_recent_songs(){
        ArrayList<Recent> list = new ArrayList<>();
        database = this.getReadableDatabase();

        String query = "select * from " + Recent.TABLE_NAME;

        Cursor itr = database.rawQuery(query, null);

        if(itr.moveToFirst()){
            do{
                Recent recent = new Recent(itr.getString(itr.getColumnIndex(Recent.SONG_ID)),
                        itr.getString(itr.getColumnIndex(Recent.TIMESTAMP)));
                list.add(recent);
            }while (itr.moveToNext());
        }
        itr.close();
        database.close();

        return list;
    }

    public int get_recent_songs_count() {
        String countQuery = "SELECT  * FROM " + Recent.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean contains_in_recent(String song_path){
        database = this.getReadableDatabase();
        Cursor qcursor = database.query(Recent.TABLE_NAME, new String[]{Recent.SONG_ID}, Recent.SONG_ID + "=?",
                new String[]{song_path}, null, null, null, null);

        String id = "-1";
        try {
            if (qcursor != null) {
                qcursor.moveToFirst();
                id = qcursor.getString(qcursor.getColumnIndex(Recent.SONG_ID));
                qcursor.close();
            }
        }catch (Exception e){
            Log.d("id_to_song", "id_to_song: " + e.toString());
            id = "-1";
        }
        return !id.equals("-1");
    }

    public void delete_recent_song_by_id(String recent_song_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Recent.TABLE_NAME, Recent.SONG_ID + " =?",
                new String[]{recent_song_id});
        db.close();
    }

    public void delete_old_song_by_id() {
        database = this.getWritableDatabase();

        String query = "select * from " + Recent.TABLE_NAME + " order by " + Recent.TIMESTAMP + " limit 1";
        Cursor bet = database.rawQuery(query, null);

        String path = "nu";
        if(bet != null){
            bet.moveToFirst();
            path = bet.getString(bet.getColumnIndex(Recent.SONG_ID));
        }
        Log.d("recent-list-path", "delete_old_song_by_id: " + path);
        if(!path.equals("nu")) {
            database.delete(Recent.TABLE_NAME, Recent.SONG_ID + " = ?",
                    new String[]{path});

        }
        database.close();
    }

    public long insert_playlistinfo(String playlist_name){
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PlaylistInfo.PLAYLIST_NAME, playlist_name);
        long id = database.insert(PlaylistInfo.TABLE_NAME, null, values);

        database.close();
        return id;
    }

    public ArrayList<PlaylistInfo> get_all_playlistinfo(){
        ArrayList<PlaylistInfo> list = new ArrayList<>();
        database = this.getReadableDatabase();

        String query = "select * from " + PlaylistInfo.TABLE_NAME;

        Cursor itr = database.rawQuery(query, null);

        if(itr.moveToFirst()){
            do{
                PlaylistInfo playlistInfo = new PlaylistInfo(itr.getString(itr.getColumnIndex(PlaylistInfo.PLAYLIST_NAME)), 0,
                        itr.getString(itr.getColumnIndex(PlaylistInfo.TIMESTAMP)));
                list.add(playlistInfo);
            }while (itr.moveToNext());
        }
        itr.close();
        database.close();

        return list;
    }

    public int get_playlistinfo_count() {
        String countQuery = "SELECT  * FROM " + PlaylistInfo.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean contains_in_playlistinfo(String playlist_name){
        database = this.getReadableDatabase();
        Cursor qcursor = database.query(PlaylistInfo.TABLE_NAME, new String[]{PlaylistInfo.PLAYLIST_NAME}, PlaylistInfo.PLAYLIST_NAME+ "=?",
                new String[]{playlist_name}, null, null, null, null);

        String id = "-1";
        try {
            if (qcursor != null) {
                qcursor.moveToFirst();
                id = qcursor.getString(qcursor.getColumnIndex(PlaylistInfo.PLAYLIST_NAME));
                qcursor.close();
            }
        }catch (Exception e){
            Log.d("id_to_song", "id_to_song: " + e.toString());
            id = "-1";
        }
        return !id.equals("-1");
    }

    public void delete_playlistinfo_by_id(String playlist_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PlaylistInfo.TABLE_NAME, PlaylistInfo.PLAYLIST_NAME + " =?",
                new String[]{playlist_name});
        db.close();
    }

    public ArrayList<String> str_fav(ArrayList<Favourite> list){
        ArrayList<String> li = new ArrayList<>();
        for(Favourite fr : list){
            li.add(fr.get_song_id());
        }return li;
    }

    public ArrayList<String> str_recent(ArrayList<Recent> list){
        ArrayList<String> li = new ArrayList<>();
        for(Recent fr : list){
            li.add(fr.get_song_id());
        }return li;
    }

    public ArrayList<String> str_playlistinfo(ArrayList<PlaylistInfo> list){
        ArrayList<String> li = new ArrayList<>();
        for(PlaylistInfo fr : list){
            li.add(fr.get_playlist_name());
        }return li;
    }

}

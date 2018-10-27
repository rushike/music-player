package p.ru.musicplayer.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;

import p.ru.musicplayer.Song;
import p.ru.musicplayer.SongPlayer;

public class AudioPlayer {
    public ArrayList<String> song_list;

    public String path = null;

    public Song song = null;

    public static MediaPlayer player = null;

    public AudioPlayer(){
        song_list = new ArrayList<>();
        if(player != null){
            player.release();
            player = null;
        }
        player = new MediaPlayer();
        song = new Song();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }


    public AudioPlayer(String path){
        this.path = path;
        player = new MediaPlayer();
    }

    /**
     * creates new audio object with init song list
     * @param song_list song list
     */
    public AudioPlayer(ArrayList<String> song_list){
        this.song_list = song_list;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }


    /**
     * get path to path from ArrayList
     * @param index
     */
    public void set_path(int index){
        if(player != null){
            player.release();
            player = null;
        }
        path = song_list.get(index);
    }

    public void set_path(String path){
        this.path = path;
    }

    public void set_song(Song song){
        this.song = song;
    }

    public void set_song_list(ArrayList list){
        song_list = list;
    }

    public void init_media_player(Context context){
        if(path != null){
            Log.d("init_media_player", Uri.parse(path).toString());
            player = MediaPlayer.create((SongPlayer)context, Uri.parse(path));
            player.start();
//            try{
//                player.setDataSource(path);
//            }catch (IOException io){
//                Log.e("init_media_player", "Io Exception due path error");
//                io.printStackTrace();
//            }
        }
    }

    public void create(Context context, String path){
        player = MediaPlayer.create(context, Uri.parse(path));
    }

    public int getCurrentPosition(){return  0;}
    public int getDuration(){return 0;}
    public int getVideoHeight(){return 0;}
    public int getVideoWidth(){return  0;}
    public void setAudioAttributes(AudioAttributes attributes){}
    public void setLooping(boolean looping){}
    public void setVolume(float x1, float x2){}

    public void pause(){
        if(player != null){
            player.pause();
        }else Log.e("pause", "player is null");
    }

    public void start(){
        if(player != null){
            Log.d("media_player_state", "State : " + player.isPlaying());
            player.start();
            Log.d("media_player_state", "State : " + player.isPlaying());
        }else Log.e("start", "player is null");
    }

    public void stop(){
        if(player != null){
            player.stop();
        }else Log.e("stop", "player is null");
    }

    public  void release(){
        player.release();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void seekTo(long msec, int mode){
        if(player != null){
            player.seekTo(msec, mode);
        }else Log.e("seekTo", "player is null");
    }

    public void prepare(){
        if(player != null){
            try{
                player.prepare();
            }catch (IOException io){
                Log.e("prepare", "Io Exception due not known error");
                io.printStackTrace();
            }
        }else Log.e("pause", "player is null");
    }

    public void  prepareAsync(){
        if(player != null){
            try{
                player.prepare();
            }catch (IOException io){
                Log.e("prepareAsync", "Io Exception due not known error");
                io.printStackTrace();
            }
        }else Log.e("pause", "player is null");
    }

    public void load(){
        if(player != null ){
            try{
                player.setDataSource(path);
                prepare();
                start();
            }catch (IOException io){
                Log.e("init_media_player", "Io Exception due path error");
                io.printStackTrace();
            }
        }
    }

    public void setDataSource(){
        load();
    }

    public void seek_to(int time){
        if(player != null && time < song.getDuration()){
            player.seekTo(time * 1000);
        }
    }


    public String toString(){

        StringBuilder sb = new StringBuilder("AudioPlayer ====> \n");
        sb.append("song list => " + song_list.toString());
        sb.append("path => " + path);
        sb.append("player => " + player.toString());
        return sb.toString();
    }
}


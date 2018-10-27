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

    public int index = 0;

    public boolean notplaying = true;

    public boolean change = false;

    public static MediaPlayer player = null;

    public AudioPlayer(){
        song_list = new ArrayList<>();
        if(player != null){
            player.release();
            player = null;
        }
        song = new Song();
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

    public boolean change(){
        change = !change;
        return change;
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
            notplaying = true;
            player.pause();
        }else Log.e("pause", "player is null");
    }

    public void pauser(){
        if(notplaying){
            start();
        }else{
            pause();
        }
    }

    public void start(){
        if(player != null){
            Log.d("media_player_state", "State : " + player.isPlaying());
            notplaying = false;
            player.start();
            Log.d("media_player_state", "State : " + player.isPlaying());
        }else Log.e("start", "player is null");
    }

    public void stop(){
        if(player != null){
            notplaying = true;
            player.stop();
        }else Log.e("stop", "player is null");
    }

    public String next(){
        release();
        index = Searcher.get_index(song_list, path);
        if(index != -1){
            index = (index + 1) % song_list.size();
            return song_list.get(index);
        }return null;
    }

    public String prev(){
        release();
        index = Searcher.get_index(song_list, path);
        if(index != -1){
            index = (index - 1 + song_list.size()) % song_list.size();
            return song_list.get(index);
        }return null;
    }

    public  void release(){
        player.release();
        player = null;
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

    public void load(Context context){
        if(player == null ){
            try{
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(path);
                Log.d("load-path", "load: path --> " + path);
                prepare();
                start();
                listners((SongPlayer) context);
            }catch (IOException io){
                Log.e("init_media_player", "Io Exception due path error");
                io.printStackTrace();
            }
        }
    }

    public void listners(final SongPlayer sp){
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                change();
                sp.load(next());
            }
        });
    }

    public void setDataSource(){

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


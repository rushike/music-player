package p.ru.musicplayer.utility;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Array;
import java.util.*;
import java.io.*;
public class Mp3Lister{
    String def_song = "/storage/emulated/0/Download/01 Jeene Laga Hoon (Ramaiya Vastavaiya) Atif.mp3";
    ArrayList<String> dirs;
    public Mp3Lister(String... dir){
        dirs = new ArrayList<String>();
        dirs.addAll(Arrays.asList(dir));
    }
    @Nullable
    public  ArrayList<String> get_song_list(File root_file) {
        ArrayList<String> fileList = new ArrayList<>();
        ArrayList<String> arr = new ArrayList<>();
        try {
            File[] files = root_file.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            Log.d("list--mp3", Arrays.toString(files));
            for (File file : files) {
                if (file.isDirectory()) {
                    if(contains_in_list(file.getName())){
                        arr.clear();
                        arr.addAll(get_song_list(file));
                        if (get_song_list(file) != null) {
                             fileList.addAll(arr);
                        } else {
                            break;
                        }
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    Log.d("mp3--got--","file:--> " + file.getName());
                    fileList.add(file.getAbsolutePath());
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean contains_in_list(String nam){
        for(String nm : dirs){
            if(nam.equalsIgnoreCase(nm)) return true;
        }return false;
    }

    @NonNull
    public static String print_line_list(ArrayList<HashMap<String, String>> song_arr, String root){
        StringBuilder list = new StringBuilder("Mp3 from root " + root + " :: root is\n-----------------------------------------------------------------------------------\n");
        try {
            for(HashMap<String, String> item : song_arr){
                try{
                    list.append(item.toString() + ",\n");
                }catch (Exception e){
                    Log.d("for-->hashmap"," :: "+item);
                }
            }list.append("-----------------------------------------------------------------------------------\n");
        }catch (Exception e){
            Log.e("Exception-->"+e, ":: Exception");
            e.printStackTrace();
        }
        return list.toString();
    }
}
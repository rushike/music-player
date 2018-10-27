package p.ru.musicplayer.utility;

import java.util.ArrayList;

public class Searcher {
    public static ArrayList<String> search_in_list(ArrayList<String> list, String query){
        ArrayList<String> slist = new ArrayList<>();
        int index = 0;
        for(String path : list){
            if(is_substring(query, path, true)){
                slist.add(index++, path);
            }
        }
        return slist;
    }

    /**
     * return boolean value if string one is substring of string two
     * @param one pattern string
     * @param two main string
     * @param ignore_case if case doesn't matter
     * @return default false
     */
    public static boolean is_substring(String one, String two, boolean ignore_case){
        if(one.length() > two.length()) return false;
        if(ignore_case) return two.toLowerCase().contains(one.toLowerCase());
        return two.contains(one);
    }
}

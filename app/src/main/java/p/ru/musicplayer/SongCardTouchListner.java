package p.ru.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SongCardTouchListner implements RecyclerView.OnItemTouchListener {

    private GestureDetector gesture_detector;
    private OnClick click;

    public SongCardTouchListner(Context context, final RecyclerView song_card_recycler_view, final OnClick click_listener){
        click = click_listener;
        gesture_detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public  boolean onSingleTapUp(MotionEvent event){
                return true;
            }
            @Override
            public void onLongPress(MotionEvent event){
                View child = song_card_recycler_view.findChildViewUnder(event.getX(), event.getY());
                if(child != null && click != null){
                    click.onLongClick(child, song_card_recycler_view.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView search_recycler_view, @NonNull MotionEvent event) {
        View child = search_recycler_view.findChildViewUnder(event.getX(), event.getY());
        if(child != null && click != null && gesture_detector.onTouchEvent(event)){
            click.onClick(child, search_recycler_view.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface OnClick{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}


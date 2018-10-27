package p.ru.musicplayer.utility;

import android.util.Log;
import android.widget.SeekBar;

public class SeekAnimate implements Runnable {

    Thread seek_thread;

    SeekBar seek_bar;

    AudioPlayer player;

    int position = 0;

    long position_time = System.currentTimeMillis() / 1000;

    public SeekAnimate(SeekBar seek_bar, AudioPlayer player){
        seek_thread = new Thread(this);
        seek_thread.setName("seek_thread");
        this.seek_bar = seek_bar;
        this.player = player;
        seek_thread.start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    position = progress;
                    position_time = System.currentTimeMillis() / 1000;
                    player.seek_to(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Log.d("seek_bar_pos", "Pos --> " + position);

        Log.d("seek_bar_pos", "Pos Time --> " + position_time);

        Log.d("seek_bar_pos", "Pos Curr --> " + (System.currentTimeMillis() / 1000));

        Log.d("seek_bar_pos", "Pos Cal--> " + (int)(position + (System.currentTimeMillis() / 1000 - position_time)));

        while(true) {
            seek_bar.setProgress((int) (position + (System.currentTimeMillis() / 1000 - position_time)));
//        try {
//            seek_thread.wait(200);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        }
    }

    public void on_destroy(){
        seek_thread.interrupt();
    }
}

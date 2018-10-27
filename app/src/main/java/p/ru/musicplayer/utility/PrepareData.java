package p.ru.musicplayer.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import p.ru.musicplayer.DataSongView;
import p.ru.musicplayer.SongAdapter;

public class PrepareData implements Runnable {

    RecyclerView rview;

    Thread tp;

    ArrayList<String> list;

    Context context;

    SongAdapter adapter;

    ArrayList<DataSongView> song_list = new ArrayList<>();

    public PrepareData(Context context, RecyclerView rview, ArrayList<String> list){
        this.context = context;
        this.rview = rview;
        this.list = list;

        //RecyclerView.LayoutManager

        adapter = new SongAdapter(context, song_list);
        RecyclerView.LayoutManager m_layout_manager  = new GridLayoutManager(context, 2);
        rview.setLayoutManager(m_layout_manager);
        rview.addItemDecoration( new GridSpacingItemDecoration(2, dpToPx(10), true));
        rview.setItemAnimator(new DefaultItemAnimator());
        rview.setAdapter(adapter);

        tp = new Thread(this);
        tp.start();
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
        File file;
        MediaMetadataRetriever meta_data;
        DataSongView data;
        byte[] byter;
        int ct = 0, ctt;
        String file_name;

        for(int i = 0; i < list.size(); i++){
            meta_data = new MediaMetadataRetriever();
            meta_data.setDataSource(list.get(i));
            byter = meta_data.getEmbeddedPicture();
            file = new File(list.get(i));
            if(byter.length > 0){
                data = new  DataSongView(list.get(i), file.getName(),
                        meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), BitmapFactory.decodeByteArray(byter, 0, byter.length));
                song_list.add(data);
            }else{
                data = new  DataSongView(list.get(i), file.getName(),
                        meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), text_as_bitmap("\u090b\n" + file.getName(), 20, 0x0000ef));
                song_list.add(data);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * var file : File
     *         var meta_data = MediaMetadataRetriever()
     *         var byter : ByteArray?
     *         var ct = 0
     *         var ctt : Int
     *         var file_name : String
     *         for(list_item : String in list){
     *             meta_data = MediaMetadataRetriever()
     *             //Log.d("prepare_data", list_item.toString())
     *             meta_data.setDataSource(list_item)
     *             byter = meta_data.embeddedPicture
     *             if(byter != null){
     *                 ctt = list_item.lastIndexOf("/")
     *                 file_name = list_item.substring(ctt + 1)
     *                 Log.w("path_name_", "fil $file_name")
     *                 var data = DataSongView(list_item!!, file_name,
     *                         meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), BitmapFactory.decodeByteArray(byter, 0, byter.size))
     *                 song_list.add(data)
     *                 //Log.d("prepare_data","lhh " + data.toString())
     *                 if(ct++ % 20 == 19) adapter.notifyDataSetChanged()
     *             }
     * //            }else {
     * //                song_list.add(DataSongView(list_item.get("file_name")!!, list_item.get("file_path")!!,
     * //                        meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), Bitmap.(imageView.setImageResource(R.drawable.abc_btn_radio_material))))
     * //            }
     *             meta_data.release()
     *         }
     *         adapter.notifyDataSetChanged()
     */

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public static Bitmap text_as_bitmap(String text, float text_size, int text_color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(text_size);
        paint.setColor(text_color);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}

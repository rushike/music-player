package p.ru.musicplayer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.os.ParcelUuid
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_music_player.*
import java.io.File
import android.util.TypedValue
import android.support.v7.widget.DefaultItemAnimator
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.app_bar_music_player.*
import kotlinx.android.synthetic.main.content_music_player.*
import p.ru.musicplayer.utility.*
import java.nio.file.Paths


class MusicPlayer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var song_list : ArrayList<DataSongView> = ArrayList()
    private  var adapter : SongAdapter = SongAdapter(this, song_list)
    private var liss : ArrayList<String> = ArrayList()

    private var dbh = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
//        setSupportActionBar(collapsing_toolbar)
//
//        val toggle = ActionBarDrawerToggle(this, drawer_layout, collapsing_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        adapter = SongAdapter(this, song_list)
        var m_layout_manager : RecyclerView.LayoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager
        var tryui = findViewById<RecyclerView>(R.id.song_list__recycler_view)
        tryui.layoutManager = m_layout_manager
        tryui.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        tryui.itemAnimator = DefaultItemAnimator()
        tryui.adapter = adapter

        var bundle = intent.extras
        try {
            var nav_sel = bundle!!.getString("nav_sel")

            if (nav_sel != null) {
                if (nav_sel.equals("recent")) {
                    liss = bundle.getStringArrayList("recent_list")!!
                    if (liss == null) liss = list_all_mp3()
                } else if (nav_sel.equals("fav")) {
                    liss = bundle.getStringArrayList("fav_list")!!
                    if (liss == null) liss = list_all_mp3()
                } else liss = list_all_mp3()
            } else liss = list_all_mp3()
        }catch (e : java.lang.Exception){
            e.printStackTrace()
            Log.e("err_bundle_null", e.toString())
            liss = list_all_mp3()
        }

        object : Thread("prepare_data"){
            override fun run() {
                prepare_data(liss)
            }
        }.start()
        var lis : ArrayList<String>


        findViewById<ImageView>(R.id.search_button_home).setOnClickListener{
            var query = findViewById<EditText>(R.id.search_bar_home).text.toString()
            lis = Searcher.search_in_list(liss, query.trim())
            Log.d("search_list","--> " + lis.toString())
            song_list.clear()
            prepare_data(lis)
        }

        song_list__recycler_view.addOnItemTouchListener(SongCardTouchListner(applicationContext, song_list__recycler_view, object : SongCardTouchListner.OnClick{
            override fun onClick(view: View?, position: Int) {
                val intent = Intent(this@MusicPlayer, SongPlayer::class.java)
                var song = song_list.get(position)
                Log.w("song-test-onclick","song-obj $song")
                intent.putExtra("song_path", song.file_path)
                intent.putExtra("song_list", liss)
                startActivity(intent)
            }

            override fun onLongClick(view: View?, position: Int) {
                onClick(view, position)
            }

        }))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.music_player, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent = Intent(this, MusicPlayer::class.java)
        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_creat_playlist -> {

            }
            R.id.nav_recent_played -> {
                intent.putExtra("nav_sel", "recent")
                intent.putExtra("recent_list", dbh.str_recent(dbh._all_recent_songs))
                startActivity(intent)
            }
            R.id.nav_recomend -> {
                intent.putExtra("nav_sel", "recent")
                intent.putExtra("recent_list", dbh.str_recent(dbh._all_recent_songs))
                startActivity(intent)
            }
            R.id.nav_liked -> {
                intent.putExtra("nav_sel", "fav")
                intent.putExtra("fav_list", dbh.str_fav(dbh._all_favourite_songs))
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun list_all_mp3() : ArrayList<String> {
        val root : File = Environment.getExternalStorageDirectory()
        var lister = Mp3Lister("Music", "Download", "SHAREit", "Whatsapp")
        var song_arr =  lister.get_song_list(root)
        Log.w("root-path","Root path --> ${root.absolutePath}")
        Log.w("song-mp3-list ", "len : ${song_arr?.size} " +song_arr.toString())
        if(song_arr == null) return ArrayList<String>()
        return song_arr
    }

    fun prepare_data(list : ArrayList<String>){
        var file: File
        var meta_data: MediaMetadataRetriever?
        var data: DataSongView
        var byter: ByteArray?
        var bitm : Bitmap?
        val ct = 0
        val ctt: Int
        val file_name: String
        var arti : String

        for (list_item : String in list){
            meta_data = MediaMetadataRetriever()
            meta_data.setDataSource(list_item)
            byter = try{
                meta_data.embeddedPicture
            }catch (e : java.lang.Exception){
                e.printStackTrace()
                null
            }
            file = File(list_item)
            arti = try{
                meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            }catch (e : Exception){
                e.printStackTrace()
                ""
            }
            if (byter != null) {
                bitm = BitmapFactory.decodeByteArray(byter, 0, byter.size)
                runOnUiThread {
                    data = DataSongView(list_item, file.name, arti, bitm)
                    song_list.add(data)
                }

            } else {
                bitm = PrepareData.text_as_bitmap("\u090b\n" + file.name, 20f, 0x0000ef)
                runOnUiThread(object : Runnable{
                    override fun run() {
                        data = DataSongView(list_item, file.name, arti, bitm)
                        song_list.add(data)
                    }
                })
            }
        }
        this.runOnUiThread { adapter.notifyDataSetChanged() }
    }


//    fun prepare_data(list : ArrayList<String>){
//        var file : File
//        var meta_data = MediaMetadataRetriever()
//        var byter : ByteArray?
//        var ct = 0
//        var ctt : Int
//        var file_name : String
//        for(list_item : String in list){
//            meta_data = MediaMetadataRetriever()
//            //Log.d("prepare_data", list_item.toString())
//            meta_data.setDataSource(list_item)
//            byter = meta_data.embeddedPicture
//            if(byter != null){
//                ctt = list_item.lastIndexOf("/")
//                file_name = list_item.substring(ctt + 1)
//                Log.w("path_name_", "fil $file_name")
//                var data = DataSongView(list_item!!, file_name,
//                        meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), BitmapFactory.decodeByteArray(byter, 0, byter.size))
//                song_list.add(data)
//                //Log.d("prepare_data","lhh " + data.toString())
//                if(ct++ % 20 == 19) adapter.notifyDataSetChanged()
//            }
////            }else {
////                song_list.add(DataSongView(list_item.get("file_name")!!, list_item.get("file_path")!!,
////                        meta_data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), Bitmap.(imageView.setImageResource(R.drawable.abc_btn_radio_material))))
////            }
//            meta_data.release()
//        }
//        adapter.notifyDataSetChanged()
//
//    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }
}

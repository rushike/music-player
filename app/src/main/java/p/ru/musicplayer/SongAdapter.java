package p.ru.musicplayer;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ru on 18/05/16.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongCardViewHolder> {

    private Context mContext;
    private List<DataSongView> song_list;

    public class SongCardViewHolder extends RecyclerView.ViewHolder {
        public TextView song_name, artist_name;
        public ImageView thumbnail, overflow;

        public SongCardViewHolder(View view) {
            super(view);
            song_name = (TextView) view.findViewById(R.id.song_name);
            artist_name = (TextView) view.findViewById(R.id.artist_name);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public SongAdapter(Context mContext, List<DataSongView> song_list) {
        this.mContext = mContext;
        this.song_list = song_list;
    }

    @Override
    public SongCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card, parent, false);

        return new SongCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongCardViewHolder holder, int position) {
        DataSongView song = song_list.get(position);
        holder.song_name.setText(song.getSong_name());
        holder.artist_name.setText(song.getArtist_name());

        // loading album cover using Glide library
        holder.thumbnail.setImageBitmap(song.getThumbnail());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_song, popup.getMenu());
        popup.setOnMenuItemClickListener(new DotsSongCardClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class DotsSongCardClickListener implements PopupMenu.OnMenuItemClickListener {

        public DotsSongCardClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return song_list.size();
    }
}
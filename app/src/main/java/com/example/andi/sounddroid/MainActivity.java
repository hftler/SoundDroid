package com.example.andi.sounddroid;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.andi.sounddroid.com.example.andi.sounddroid.sub.SoundCloud;
import com.example.andi.sounddroid.com.example.andi.sounddroid.sub.SoundCloudService;
import com.example.andi.sounddroid.com.example.andi.sounddroid.sub.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    public static final String TAG ="bingo";

    private TracksAdapter mAdapter;
    private List<Track> mTracks;
    private TextView mSelectedTitle;
    private ImageView mSelectedThumbnail;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerState;
    private SearchView mSearchView;
    private List<Track> mPreviousTracks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        mSelectedTitle = (TextView)findViewById(R.id.selected_title);
        mSelectedThumbnail = (ImageView)findViewById(R.id.selected_thumbnail);
        mPlayerState = (ImageView) findViewById(R.id.player_state);
        mPlayerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleState();
            }
        });
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                toggleState();
            }
        });


        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerState.setImageResource(R.drawable.ic_play);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.songs_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTracks = new ArrayList<Track>();
        mAdapter = new TracksAdapter(this,mTracks);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = mTracks.get(position);

                mSelectedTitle.setText(selectedTrack.getTitle());
                Picasso.with(MainActivity.this).load(selectedTrack.getAvatarURL()).into(mSelectedThumbnail);

                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    mMediaPlayer.setDataSource(selectedTrack.getStreamURL()+"?client_id="+SoundCloudService.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        recyclerView.setAdapter(mAdapter);



        SoundCloudService service = SoundCloud.getSoundCloudService();
        service.getRecentSongs(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                Log.d(TAG, "Track title is " + tracks.get(0).getTitle());
               updateTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    public void toggleState(){
        if(mMediaPlayer.isPlaying()){
            mPlayerState.setImageResource(R.drawable.ic_play);
            mMediaPlayer.pause();
        }else{
            mPlayerState.setImageResource(R.drawable.ic_pause);
            mMediaPlayer.start();
        }


    }

    private void updateTracks(List<Track> tracks){
        mTracks.clear();
        mTracks.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        SoundCloud.getSoundCloudService().searchSongs(query,new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }

            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchView =(SearchView) menu.findItem(R.id.search_view).getActionView();
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search_view), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mPreviousTracks = new ArrayList<Track>(mTracks);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateTracks(mPreviousTracks);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_view) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
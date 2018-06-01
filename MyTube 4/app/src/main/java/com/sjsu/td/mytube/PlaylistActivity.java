package com.sjsu.td.mytube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Handler handler;
    private ArrayList<VideoItem> searchResults;
    private RecyclerViewClickListener listener;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //initializations
        searchInput = (EditText)findViewById(R.id.search_input);
        handler = new Handler();
        searchResults = new ArrayList<>();
        //dataHolder = new DataHolder();


        //use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        listener = (view, position) -> {
            String videoTitle = searchResults.get(position).getTitle().toString();
            Toast.makeText(this, "Playing Now:\n " + videoTitle, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlaylistActivity.this, YoutubePlayerActivity.class);
            intent.putExtra("VIDEO_ID", searchResults.get(position).getId());
            startActivity(intent);
        };

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                    if (v.getText().toString() != "")
                        searchOnYoutube(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                    return false;
                }
                return true;
            }
        });
         searchOnYoutube("android studio");

    }
    private void searchOnYoutube(final String searchKey) {
        new Thread() {
            public void run() {
                YoutubeLinker youtubeLinker = new YoutubeLinker(PlaylistActivity.this);
                searchResults = youtubeLinker.search(searchKey);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    public void updateVideosFound() {
        mAdapter = new MyAdapter(this,searchResults, listener);
        mRecyclerView.setAdapter(mAdapter);
    }

}


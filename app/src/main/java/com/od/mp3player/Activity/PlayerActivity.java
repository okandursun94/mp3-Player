package com.od.mp3player.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.od.mp3player.Adapter.MusicAdapter;
import com.od.mp3player.MainActivity;
import com.od.mp3player.Model.Song;
import com.od.mp3player.R;

import java.util.ArrayList;
import java.util.Random;

import static com.od.mp3player.MainActivity.repeatOn;
import static com.od.mp3player.MainActivity.shuffleOn;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView tv_songName, tv_durationPlayed ,tv_durationTotal;
    ImageView img_coverArt, img_nextBtn, img_prevBtn, img_shuffle, img_repeat;
    FloatingActionButton btn_playPause;
    SeekBar seekBar;

    int position = -1;
    static ArrayList<Song> songList = new ArrayList<>();
    static MediaPlayer mediaPlayer ;
    static Uri uri;

    private Handler handler = new Handler();
    private Thread playThread, nextThread, prevThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        findComponents();
        initialization();
        mediaPlayer.setOnCompletionListener(this);
        setOnClickListener();

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    tv_durationPlayed.setText(formattedDuration(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();

        super.onResume();
    }

    private void prevThreadBtn() {

        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                img_prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void nextThreadBtn() {

        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                img_nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void playThreadBtn() {

        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btn_playPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){

            btn_playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else {
            btn_playPause.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
            seekBar.setProgress(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private void nextBtnClicked() {

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleOn && !repeatOn){
                position = getRandom(songList.size() - 1);
            }else if(!shuffleOn && !repeatOn){
                position = (position+1)%songList.size();
            }

            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tv_songName.setText(songList.get(position).getTitle());
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_playPause.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleOn && !repeatOn){
                position = getRandom(songList.size() - 1);
            }else if(!shuffleOn && !repeatOn){
                position = (position+1)%songList.size();
            }
            position = (position+1)%songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tv_songName.setText(songList.get(position).getTitle());
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){

            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleOn && !repeatOn){
                position = getRandom(songList.size() - 1);
            }else if(!shuffleOn && !repeatOn){
                position = ((position - 1) < 0 ? (songList.size() - 1) : (position-1));
            }

            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tv_songName.setText(songList.get(position).getTitle());
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_playPause.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleOn && !repeatOn){
                position = getRandom(songList.size() - 1);
            }else if(!shuffleOn && !repeatOn){
                position = ((position - 1) < 0 ? (songList.size() - 1) : (position-1));
            }

            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tv_songName.setText(songList.get(position).getTitle());
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition =mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private void initialization(){
        position= getIntent().getIntExtra("position",-1);
        songList = MainActivity.songArrayList;
        if(songList != null){
            btn_playPause.setImageResource(R.drawable.ic_baseline_pause_24);
            uri = Uri.parse(songList.get(position).getPath());
        }

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);

        tv_songName.setText(songList.get(position).getTitle());
    }

    private void findComponents(){
        tv_songName = findViewById(R.id.song_name);
        tv_durationPlayed = findViewById(R.id.duration_played);
        tv_durationTotal = findViewById(R.id.duration_total);
        img_coverArt = findViewById(R.id.cover_art);
        img_nextBtn = findViewById(R.id.id_next);
        img_prevBtn = findViewById(R.id.id_prev);
        img_shuffle = findViewById(R.id.id_shuffle);
        img_repeat = findViewById(R.id.repeat);
        btn_playPause = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekbar);
    }

    private void setOnClickListener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i *1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        img_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shuffleOn){
                    shuffleOn=false;
                    img_shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24_off);
                }else {
                    shuffleOn=true;
                    img_shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24_on);
                }
            }
        });

        img_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatOn){
                    repeatOn=false;
                    img_repeat.setImageResource(R.drawable.ic_baseline_repeat_24_off);
                }else {
                    repeatOn=true;
                    img_repeat.setImageResource(R.drawable.ic_baseline_repeat_24_on);
                }
            }
        });
    }

    private String formattedDuration(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition%60);
        String minutes = String.valueOf(mCurrentPosition /60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0"+seconds;
        if(seconds.length() == 1){
            return totalNew;
        }else {
            return totalOut;
        }

    }

    private void metaData(Uri uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(songList.get(position).getDuration())/1000;
        tv_durationTotal.setText(formattedDuration(durationTotal));
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        if(art != null){
            Glide.with(this).asBitmap().load(art).into(img_coverArt);
        }else {
            Glide.with(this).asBitmap().load(R.drawable.sharp_music_note_24).into(img_coverArt);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextBtnClicked();
        if(mediaPlayer !=null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
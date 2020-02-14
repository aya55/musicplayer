package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class mPlayer extends AppCompatActivity {
 Button btn_next,btn_pause,btn_previous;
 TextView songTextlabel;
 SeekBar seekBar;
 static MediaPlayer MymediaPlayer;
 int position;
 String sname;
 ArrayList<File>mysongs;
 Thread updateseekbar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_player);
      btn_next=findViewById(R.id.next);
      btn_pause=findViewById(R.id.pause);
      btn_previous=findViewById(R.id.previos);
      seekBar=findViewById(R.id.seekbar);
      songTextlabel=findViewById(R.id.txtt);
      getSupportActionBar().setTitle("Now playing");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      updateseekbar=new Thread(){

          @Override
          public void run() {
              int totalDuoritin = MymediaPlayer.getDuration();
              int currentposition = 0;
              while (currentposition < totalDuoritin) {

                  try {
                      sleep(500);
                      currentposition = MymediaPlayer.getCurrentPosition();
                      seekBar.setProgress(currentposition);
                  } catch (InterruptedException e) {

                      e.printStackTrace();
                  }
              }
          }
      };
      if (MymediaPlayer!=null){
   MymediaPlayer.stop();
   MymediaPlayer.release();

      }
        Intent i=getIntent();
      Bundle bundle=i.getExtras();
      mysongs=(ArrayList)bundle.getParcelableArrayList("songs");
      sname=mysongs.get(position).getName().toString();
      String songsname=i.getStringExtra("Songs name");
      songTextlabel.setText(songsname);
      songTextlabel.setSelected(true);
      position=bundle.getInt("pos",0);
        final Uri uri=Uri.parse(mysongs.get(position).toString());
        MymediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        MymediaPlayer.start();
        seekBar.setMax(MymediaPlayer.getDuration());
         updateseekbar.start();
         seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
         seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             MymediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(MymediaPlayer.getDuration());
                if (MymediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.play);
                    MymediaPlayer.pause();

                }
                else {
                  btn_pause.setBackgroundResource(R.drawable.pause);
                  MymediaPlayer.start();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MymediaPlayer.stop();
                MymediaPlayer.release();
                position=((position+1)%mysongs.size());
                Uri uri1=Uri.parse(mysongs.get(position).toString());
                MymediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=mysongs.get(position).getName().toString();
                songTextlabel.setText(sname);
                MymediaPlayer.start();

            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MymediaPlayer.stop();
                MymediaPlayer.release();
                position=((position-1<0)?(mysongs.size()-1):(position-1));
                Uri uri1=Uri.parse(mysongs.get(position).toString());
                MymediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=mysongs.get(position).getName().toString();
                songTextlabel.setText(sname);
                MymediaPlayer.start();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

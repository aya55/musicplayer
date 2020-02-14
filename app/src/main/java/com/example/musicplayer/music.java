package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class music extends AppCompatActivity {
ListView listView;
String[]items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
       listView=findViewById(R.id.listv);
       runtime();
    }
    public void runtime(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
             display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
              token.continuePermissionRequest();
            }
        }).check();

    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for (File singleFiles: files){
        if (singleFiles.isDirectory()&&!singleFiles.isHidden()){
            arrayList.addAll(findsong(singleFiles));

        }
         else {

             if (singleFiles.getName().endsWith(".mp3")||singleFiles.getName().endsWith(".wav")){
                 arrayList.add(singleFiles);
             }
        }
        }
      return arrayList;
    }
    void display(){
        final ArrayList<File> mysongs=findsong(Environment.getExternalStorageDirectory());
       items=new String[mysongs.size()];
       for (int i=0;i<mysongs.size();i++){
           items [i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
       }
        ArrayAdapter<String> myadapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        listView .setAdapter(myadapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songsName=listView.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("Songs",mysongs)
                        .putExtra("Songs name",songsName).putExtra("pos",position));

            }
        });







    }
}

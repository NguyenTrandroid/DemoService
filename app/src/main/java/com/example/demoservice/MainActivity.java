package com.example.demoservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btStart, btStop, btnNext, btnPre;
    private List<Song> listSong;
    private boolean statePlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addSong();
        initView();
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statePlay) {
                    btStart.setText("Pause");
                    statePlay = false;
                } else {
                    btStart.setText("Play");
                    statePlay = true;
                }
                Intent service = new Intent(MainActivity.this, MyService.class);
                service.putExtra("LIST", (Serializable) listSong);
                service.setAction(Constants.ACTION.PLAY_ACTION);
                startService(service);

            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePlay=true;
                btStart.setText("Play");
                Intent service = new Intent(MainActivity.this, MyService.class);
                service.putExtra("LIST", (Serializable) listSong);
                service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(service);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(MainActivity.this, MyService.class);
                service.putExtra("LIST", (Serializable) listSong);
                service.setAction(Constants.ACTION.NEXT_ACTION);
                startService(service);

            }
        });
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(MainActivity.this, MyService.class);
                service.putExtra("LIST", (Serializable) listSong);
                service.setAction(Constants.ACTION.PREV_ACTION);
                startService(service);
            }
        });

    }

    private void initView() {
        btStart = (Button) findViewById(R.id.btnPlay);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnNext = (Button) findViewById(R.id.btnNext);
        btStop = (Button) findViewById(R.id.btnStop);
    }

    private void addSong() {
        listSong = new ArrayList<>();
        listSong.add(new Song(R.raw.ve, "Ve"));
        listSong.add(new Song(R.raw.cochangtraivietlencay, "co chang trai viet len cay"));
        listSong.add(new Song(R.raw.haitrieunam, "Hai trieu nam"));
        listSong.add(new Song(R.raw.hongkong12, "Hong Kong 1 2"));
        listSong.add(new Song(R.raw.thuantheoytroi, "Thuan theo y troi"));
    }

}

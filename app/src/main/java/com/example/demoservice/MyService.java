package com.example.demoservice;

import android.app.Notification;
import android.app.PendingIntent;

import android.app.Service;
import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean statePlay = true;
    private List<Song> listSong;
    private int indexSong;


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        listSong = new ArrayList<>();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(listSong.isEmpty()){
            listSong = (List<Song>) intent.getSerializableExtra("LIST");
        }


        switch (intent.getAction()) {
            case Constants.ACTION.STARTFOREGROUND_ACTION:
                mediaPlayer.start();
                break;
            case Constants.ACTION.PLAY_ACTION:
                if (statePlay) {
                    if(mediaPlayer==null){
                        indexSong = randomSong();
                        mediaPlayer = MediaPlayer.create(this, listSong.get(indexSong).file);
                        showNotification(listSong.get(indexSong).name,"Pause");
                        mediaPlayer.start();
                        statePlay=false;
                    }else{
                        mediaPlayer.pause();
                        statePlay = false;
                        showNotification(listSong.get(indexSong).name, "Play");
                    }

                } else {
                    mediaPlayer.start();
                    statePlay = true;
                    showNotification(listSong.get(indexSong).name, "Pause");
                }
                break;
            case Constants.ACTION.NEXT_ACTION:
                mediaPlayer.stop();
                mediaPlayer.release();
                indexSong = randomSong();
                mediaPlayer = MediaPlayer.create(this, listSong.get(indexSong).file);
                showNotification(listSong.get(indexSong).name, "Play");
                mediaPlayer.start();

                break;
            case Constants.ACTION.PREV_ACTION:
                mediaPlayer.stop();
                mediaPlayer.release();
                indexSong = randomSong();
                mediaPlayer = MediaPlayer.create(this, listSong.get(indexSong).file);
                showNotification(listSong.get(indexSong).name, "Play");
                mediaPlayer.start();
                break;
            case Constants.ACTION.STOPFOREGROUND_ACTION:
                mediaPlayer.stop();
                mediaPlayer.release();
                stopForeground(true);
                stopSelf();
                break;
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                statePlay=false;
                Intent service = new Intent(MyService.this, MyService.class);
                service.setAction(Constants.ACTION.PLAY_ACTION);
                startService(service);


            }
        });
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    private void showNotification(String songName, String title) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Intent previousIntent = new Intent(this, MyService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);
        Intent playIntent = new Intent(this, MyService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        Intent nextIntent = new Intent(this, MyService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("music is the best")
                .setContentText(songName)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous", ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, title, pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", pnextIntent).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }
    private int randomSong(){
        Random rd=new Random();
        return rd.nextInt(5);
    }
}

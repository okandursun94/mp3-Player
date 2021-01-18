package com.od.mp3player.Helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.od.mp3player.Model.Song;

import java.io.File;
import java.util.ArrayList;

public class FindMusic {

    public static ArrayList<Song> getAllSongs(Context context){
        ArrayList<Song> tempAudioList = new ArrayList<>();
        if(android.os.Build.VERSION.SDK_INT >= 29){
            Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Audio.Media.ARTIST
            };
            Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
            if (cursor != null) {
                while ((cursor.moveToNext())) {
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);

                    Song song = new Song(path, title, artist, album, duration);
                    tempAudioList.add(song);
                }
                cursor.close();
            }


            Uri uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor2 = context.getContentResolver().query(uri2, projection, null, null, null);
            if (cursor2 != null) {
                while ((cursor2.moveToNext())) {
                    String album = cursor2.getString(0);
                    String title = cursor2.getString(1);
                    String duration = cursor2.getString(2);
                    String path = cursor2.getString(3);
                    String artist = cursor2.getString(4);

                    Song song = new Song(path, title, artist, album, duration);
                    tempAudioList.add(song);
                }
                cursor.close();
            }

        }else {
            Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ARTIST
            };
            Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
            if (cursor != null) {
                while ((cursor.moveToNext())) {
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);

                    Song song = new Song(path, title, artist, album, duration);
                    tempAudioList.add(song);
                }
                cursor.close();
            }


            Uri uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor2 = context.getContentResolver().query(uri2, projection, null, null, null);
            if (cursor2 != null) {
                while ((cursor2.moveToNext())) {
                    String album = cursor2.getString(0);
                    String title = cursor2.getString(1);
                    String duration = cursor2.getString(2);
                    String path = cursor2.getString(3);
                    String artist = cursor2.getString(4);

                    Song song = new Song(path, title, artist, album, duration);
                    tempAudioList.add(song);
                }
                cursor.close();
            }
        }

        return tempAudioList;
    }
}

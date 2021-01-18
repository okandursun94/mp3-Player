package com.od.mp3player.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.od.mp3player.Model.Song;
import com.od.mp3player.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder>{

    private Context mContext;
    private ArrayList<Song> albumFiles;
    View view;

    public AlbumAdapter(Context mContext, ArrayList<Song> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.albumName.setText(albumFiles.get(position).getAlbum());

        byte[] image =getAlbumArt(albumFiles.get(position).getPath());

        if(image != null){
            Glide.with(mContext).asBitmap().load(image).into(holder.albumImage);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.sharp_music_note_24).into(holder.albumImage);
        }

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.img_album);
            albumName = itemView.findViewById(R.id.album_name);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return art;
    }

}

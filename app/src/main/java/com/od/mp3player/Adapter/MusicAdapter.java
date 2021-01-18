  package com.od.mp3player.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.od.mp3player.Activity.PlayerActivity;
import com.od.mp3player.Model.Song;
import com.od.mp3player.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Song> songArrayList;

    public MusicAdapter(Context mContext, ArrayList<Song> songArrayList) {
        this.mContext = mContext;
        this.songArrayList = songArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_file_name.setText(songArrayList.get(position).getTitle());
        /*byte[] image =getAlbumArt(songArrayList.get(position).getPath());

        if(image != null){
            Glide.with(mContext).asBitmap().load(image).into(holder.img_song);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_launcher_background).into(holder.img_song);
        }*/

        Glide.with(mContext).asBitmap().load(R.drawable.sharp_music_note_24).into(holder.img_song);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_file_name;
        ImageView img_song;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_file_name = itemView.findViewById(R.id.file_name);
            img_song = itemView.findViewById(R.id.img_song);
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

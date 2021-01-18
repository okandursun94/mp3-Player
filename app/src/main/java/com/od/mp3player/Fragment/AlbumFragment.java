package com.od.mp3player.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mp3player.Adapter.AlbumAdapter;
import com.od.mp3player.Adapter.MusicAdapter;
import com.od.mp3player.R;

import static com.od.mp3player.MainActivity.songArrayList;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;

    public AlbumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(songArrayList.size()>1){
            AlbumAdapter albumAdapter = new AlbumAdapter(getContext(),songArrayList);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}
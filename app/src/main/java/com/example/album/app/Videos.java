package com.example.album.app;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 *
 */
public class Videos extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_videos);

        gridView.setAdapter(new VideoAdapter(getActivity(),"Videos"));
        //gridView.setClickable(true);

        return rootView;
    }
}

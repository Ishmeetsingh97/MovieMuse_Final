package com.example.warmachine.moviemuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent onStartDetailActivity = getActivity().getIntent();
        if(onStartDetailActivity != null && onStartDetailActivity.hasExtra( getString(R.string.serializable_movie) ))
        {
            JSON extraJSON = (JSON) onStartDetailActivity.getSerializableExtra(getString(R.string.serializable_movie));

            TextView title = (TextView) rootView.findViewById(R.id.movie_title);
            title.setText( extraJSON.getOriginalTitle() );
            TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
            releaseDate.setText( extraJSON.getReleaseDate().substring(0,4) );
            TextView userRating = (TextView) rootView.findViewById(R.id.rating);
            userRating.setText( extraJSON.getUserRating() + "/10" );
            TextView plotSynopsis = (TextView) rootView.findViewById(R.id.plot_synopsis);
            plotSynopsis.setText( extraJSON.getPlotSynopsis());
            ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getActivity()).load( extraJSON.getImageUrl() ).into( poster );
        }
        return rootView;
    }
}

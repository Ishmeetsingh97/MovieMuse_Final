package com.example.warmachine.moviemuse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        updateData();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();

    }


    private void setAdapter( final ArrayList<JSON> JSONList)
    {
        GridView gridview;
        if ( getView() != null ) {

                    gridview = (GridView) getView().findViewById(R.id.gv1);
                    gridview.setAdapter(new PiccasoAdapter(getActivity(), JSONList));
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Context context = MainActivityFragment.this.getActivity();
                    Intent startMovieDetailActivity = new Intent(context, DetailActivity.class);
                    JSON extraJSON = JSONList.get(position);
                    startMovieDetailActivity.putExtra( getString(R.string.serializable_movie), extraJSON);
                    MainActivityFragment.this.startActivity(startMovieDetailActivity);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;

    }


    private static String LAST_TEST_ORDER = "";
    public void updateData()
    {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String ORDER = sharedPref.getString(getString(R.string.sort_order_key), getString(R.string.order_def_value));
            FetchJson data = new FetchJson();
            data.execute( ORDER );
            LAST_TEST_ORDER = ORDER;

    }

    public class FetchJson extends AsyncTask<String, Void, ArrayList<JSON> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute( ArrayList<JSON> JSONList) {
            if(JSONList != null) {
                MainActivityFragment.this.setAdapter(JSONList);
            }
            else
            {
                Intent offline = new Intent(getActivity(),Offline.class);
                startActivity(offline);
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        public FetchJson() {
            super();
        }

        @Override
        protected ArrayList<JSON> doInBackground(String... params) {

            String sortOrder;
            if(params.length != 0)
            {
                sortOrder = params[0];
            }
            else
            {
                return null;
            }

            String api =  buildApiRequest(sortOrder);
            String moviesJsonString = RealJson(api);
            if(moviesJsonString != null) {
                try {
                    return JSON.createMovieListFromJson(moviesJsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private String buildApiRequest(String sortOrder)
        {
            final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String API_KEY = getString( R.string.api_key );
            final String KEY_PARAM = "api_key";
            Uri.Builder api = Uri.parse(MOVIEDB_BASE_URL).buildUpon();
            api.appendEncodedPath( sortOrder );
            api.appendQueryParameter(KEY_PARAM, API_KEY);
            api.build();

            return api.toString();
        }

        private String RealJson(String api)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonString = null;

            try {
                URL url = new URL(api);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                moviesJsonString = buffer.toString();
            }
            catch (IOException e) {
                Log.e(LOG_TAG, " IOException!", e);
                e.printStackTrace();
            }
            finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return moviesJsonString;
        }
    }


}

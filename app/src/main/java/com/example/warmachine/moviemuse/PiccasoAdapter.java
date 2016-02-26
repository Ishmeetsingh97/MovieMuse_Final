package com.example.warmachine.moviemuse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PiccasoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<JSON> JSONList;

    public PiccasoAdapter(Context c, ArrayList<JSON> JSONList) {
        mContext = c;
        this.JSONList = JSONList;
    }

    public int getCount() {
        return JSONList.size();
    }

    public JSON getItem(int position) {
        return JSONList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    (int)mContext.getResources().getDimension(R.dimen.width),
                    (int)mContext.getResources().getDimension(R.dimen.height)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else
        {
            imageView = (ImageView) convertView;
        }

        String url = getItem(position).getImageUrl();
        Picasso.with(mContext).load(url).into(imageView);
        return imageView;
    }
}
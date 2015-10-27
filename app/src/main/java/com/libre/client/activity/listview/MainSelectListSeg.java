package com.libre.client.activity.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.libre.client.activity.R;

/**
 * Created by adsoft on 15. 10. 19.
 */
public class MainSelectListSeg extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] saNum;
    private final String[] saTitle;
    private final String[] saSource;
    private final Integer[] imageId;

    TextView textNumber;
    TextView textDescription;
    TextView textSourceName;
    ImageView imgDescription;

    public MainSelectListSeg(Activity context, String[] num ,String[] title, String[] source, Integer[] imageId) {
        super(context, R.layout.main_select_list_segment, num);
        this.context = context;
        this.saNum = num;
        this.saTitle = title;
        this.saSource = source;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.main_select_list_segment, null, true);

        textNumber = (TextView) rowView.findViewById(R.id.textNumber);
        textDescription = (TextView) rowView.findViewById(R.id.textDescription);
        textSourceName = (TextView) rowView.findViewById(R.id.textSourceName);
        imgDescription = (ImageView) rowView.findViewById(R.id.imgDescription);

        textNumber.setText(saNum[position]);
        textDescription.setText(saTitle[position]);
        textSourceName.setText(saSource[position]);
        imgDescription.setImageResource(imageId[position]);

//        textNumber.setTextSize(30);

        return rowView;
    }

    public TextView getTextSourceNameAtindex()
    {
        return textSourceName;
    }
}

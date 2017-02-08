package com.example.android.miwok;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ehayes on 1/22/2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResourceId;

    public WordAdapter(Context context, ArrayList<Word> word, int color) {
        super(context, 0, word);
        mColorResourceId = color;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Word word = getItem(position);
        // Lookup view for data population - xml Portion
        TextView miwok = (TextView) listViewItem.findViewById(R.id.miwok_text_view);
        TextView english = (TextView) listViewItem.findViewById(R.id.default_text_view);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.image);
        // Populate the data into the template view using the data object
        miwok.setText(word.getMiwokTranslation());
        english.setText(word.getDefaultTranslation());
        if(word.hasImage()){
            image.setImageResource(word.getImageResourceId());
            image.setVisibility(View.VISIBLE);
        }
        else {
            image.setVisibility(View.GONE);
        }

        View textContainer = listViewItem.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);
        // Return the completed view to render on screen
        return listViewItem;
    }
}

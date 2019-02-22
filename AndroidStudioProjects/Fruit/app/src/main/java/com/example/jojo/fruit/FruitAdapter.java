package com.example.jojo.fruit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.lang.Object;

public class FruitAdapter extends ArrayAdapter<Fruit> {

    /**
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context used to inflate the layout file.
     * @param fruits A List of Fruit objects to display in a list
     */
    public FruitAdapter(Activity context, ArrayList<Fruit> fruits) {
        super(context, 0, fruits);
    }

    /**
     * Provides a view for an AdapterView
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Fruit currentFruit = getItem(position);
        String fruitName = currentFruit.getName();
        String firstLetter = fruitName.substring(0, 1);

        TextView fruitNameTextView = (TextView) listItemView.findViewById(R.id.fruit_name);
        TextView fruitAvatarTextView = (TextView) listItemView.findViewById(R.id.fruit_avatar_text_view);

        if(fruitName != null) {
            fruitNameTextView.setText(StringUtils.capitalize(fruitName));
            fruitAvatarTextView.setText(firstLetter.toUpperCase());
        }

        return listItemView;
    }


}

package com.vn.utility.icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class IconAdapter extends ArrayAdapter<Integer> {
    private int selectedItemPosition = -1;
    public IconAdapter(Context context, List<Integer> icons) {
        super(context, android.R.layout.simple_list_item_1, icons);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView != null
                ? convertView
                : LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView textView = view.findViewById(android.R.id.text1);

        // Set the icon as the left compound drawable
        textView.setCompoundDrawablesWithIntrinsicBounds(getItem(position), 0, 0, 0);

        // Set padding between the icon and text
        textView.setCompoundDrawablePadding(16);

        // Set text to an empty string
        textView.setText("");

        return view;
    }
    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged(); // Refresh the ListView to reflect changes
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }
}

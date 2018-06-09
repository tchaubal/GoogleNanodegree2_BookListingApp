package com.example.tanushreechaubal.bookmark_searchlist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TanushreeChaubal on 4/30/18.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> data){
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
        }

        Book currentBookInfo = getItem(position);
        TextView bookTitle = listItemView.findViewById(R.id.title);
        bookTitle.setText(currentBookInfo.getTitle());

        TextView authors = (TextView) listItemView.findViewById(R.id.authors_1);
        authors.setText(currentBookInfo.getAuthor());

        TextView bookLanguage = listItemView.findViewById(R.id.language);
        bookLanguage.setText(currentBookInfo.getLanguage());

        TextView bookPageCount = listItemView.findViewById(R.id.pageCount);
        bookPageCount.setText(Integer.toString(currentBookInfo.getPageCount()));

        return listItemView;
    }
}

package fr.colourz.mynotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


public class CustomAdapter extends ArrayAdapter<Note>{

    private List<Note> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtContent;
        TextView txtDate;
    }

    public CustomAdapter(List<Note> data, Context context) {
        super(context, R.layout.notes_layout, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Note dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            SharedPreferences preferences = getContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
            if (preferences.getString("display_mode", "regular").matches("regular") && preferences.getString("app_theme", "light").matches("light") )
                convertView = inflater.inflate(R.layout.notes_layout, parent, false);
            else if (preferences.getString("display_mode", "regular").matches("regular") && preferences.getString("app_theme", "light").matches("dark"))
                convertView = inflater.inflate(R.layout.notes_layout_dark, parent, false);
            else if (preferences.getString("display_mode", "regular").matches("compact") && preferences.getString("app_theme", "light").matches("light"))
                convertView = inflater.inflate(R.layout.notes_layout_compact, parent, false);
            else
                convertView = inflater.inflate(R.layout.notes_layout_compact_dark, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.tv_title);
            viewHolder.txtContent = convertView.findViewById(R.id.tv_content);
            viewHolder.txtDate = convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Calendar cal = Calendar.getInstance();
        String dateModified;
        String dateCreated;
        SharedPreferences preferences = getContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        if (preferences.getString("date_format", "ddmmyyyy").matches("ddmmyyyy")) {
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateModified()) * 1000L);
            dateModified = DateFormat.format("dd/MM/yyyy", cal).toString();
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateCreated()) * 1000L);
            dateCreated = DateFormat.format("dd/MM/yyyy", cal).toString();
        } else if(preferences.getString("date_format", "ddmmyyyy").matches("mmddyyyy")) {
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateModified()) * 1000L);
            dateModified = DateFormat.format("MM-dd-yyyy", cal).toString();
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateCreated()) * 1000L);
            dateCreated = DateFormat.format("MM-dd-yyyy", cal).toString();
        } else {
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateModified()) * 1000L);
            dateModified = DateFormat.format("dd-MMM-yyyy", cal).toString();
            cal.setTimeInMillis(Long.parseLong(dataModel.getDateCreated()) * 1000L);
            dateCreated = DateFormat.format("dd-MMM-yyyy", cal).toString();
        }
        viewHolder.txtTitle.setText(dataModel.getTitle());
        if (preferences.getString("display_mode", "regular") == "regular") {
            viewHolder.txtContent.setText(Html.fromHtml(dataModel.getContent()), TextView.BufferType.SPANNABLE);
            viewHolder.txtDate.setText("Last modif.: " + dateModified + " | Created on: " + dateCreated);
        } else {
            viewHolder.txtContent.setText(Html.fromHtml(dataModel.getContent()), TextView.BufferType.SPANNABLE);
            viewHolder.txtDate.setText(dateModified);
        }
        return convertView;
    }


}
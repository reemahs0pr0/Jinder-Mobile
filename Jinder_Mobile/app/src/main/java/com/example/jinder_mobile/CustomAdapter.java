package com.example.jinder_mobile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAdapter extends ArrayAdapter {
    private final Context context;

    protected Job[] jobs;

    public CustomAdapter(Context context, int resId) {
        super(context, resId);
        this.context = context;
    }

    public void setData(Job[] jobs) {
        this.jobs = jobs;

        for (int i=0; i<jobs.length; i++) {
            add(null);
        }
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            view = inflater.inflate(R.layout.row, parent, false);
        }

        // set the text for job title
        TextView jobTitle = view.findViewById(R.id.jobTitle);
        jobTitle.setText(jobs[pos].getJobTitle());

        // set the text for company name
        TextView companyName = view.findViewById(R.id.companyName);
        companyName.setText(jobs[pos].getCompanyName());

        return view;
    }
}

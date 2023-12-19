package com.example.msd_project.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msd_project.R;
import com.example.msd_project.models.TableTeam;

import java.util.List;

public class TeamListView extends ArrayAdapter {
    private Activity context;
    List<TableTeam> tableTeamList;

    public TeamListView(Activity context, List<TableTeam> tableTeamList) {
        super(context, R.layout.teams_row_item,tableTeamList);
        this.context =context;
        this.tableTeamList = tableTeamList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView==null) {
            row = inflater.inflate(R.layout.teams_row_item, null, true);
        }
        TextView textViewTeam = (TextView) row.findViewById(R.id.textViewTeam);
        ImageView imageViewClub = (ImageView) row.findViewById(R.id.imageViewClub);

        textViewTeam.setText(tableTeamList.get(position).getTeamName());

        String imageName = tableTeamList.get(position).getImageURL();
        int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        imageViewClub.setImageResource(resID);


        return row;
    }
}

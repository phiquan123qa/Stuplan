package com.vn.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.appdesign.R;
import com.vn.models.Project;

import java.util.ArrayList;
import java.util.List;

public class AdapterProjectsList extends RecyclerView.Adapter<AdapterProjectsList.AdapterProjectsListHolder> {
    Context context;
    List<Project> listProjects;

    public AdapterProjectsList(Context context, List<Project> listProjects) {
        this.context = context;
        this.listProjects = listProjects;
    }

    @NonNull
    @Override
    public AdapterProjectsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simple, parent, false);
        return new AdapterProjectsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProjectsListHolder holder, int position) {
        Project project = listProjects.get(position);
        holder.titleProject.setText(project.getTitle());
    }

    @Override
    public int getItemCount() {
        return listProjects.size();
    }

    public static class AdapterProjectsListHolder extends RecyclerView.ViewHolder{
        TextView titleProject;

        public AdapterProjectsListHolder(@NonNull View itemView) {
            super(itemView);
            titleProject=itemView.findViewById(R.id.title_prj_item);
        }
    }
}

package com.vn.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.appdesign.HomeActivity;
import com.vn.appdesign.R;
import com.vn.models.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterProjectsList extends RecyclerView.Adapter<AdapterProjectsList.AdapterProjectsListHolder> {
    Context context;
    List<Project> listProjects;
    OnItemClickListener listener;
    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(List<Project> filteredList) {
        this.listProjects = filteredList;
        notifyDataSetChanged();
    }
    public void sortByName() {
        Collections.sort(listProjects, new Comparator<Project>() {
            @Override
            public int compare(Project project1, Project project2) {
                return project1.getTitle().compareToIgnoreCase(project2.getTitle());
            }
        });
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(String projectId);
    }

    public AdapterProjectsList(Context context, List<Project> listProjects, OnItemClickListener listener) {
        this.context = context;
        this.listProjects = listProjects;
        this.listener = listener;
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
        holder.bind(project, listener);
        int iconResourceId = getIconResourceId(context, project.getIcon());
        if (iconResourceId != 0) {
            holder.iconProject.setImageResource(iconResourceId);
        }
    }
    private int getIconResourceId(Context context, String iconName) {
        if(iconName != null && !iconName.isEmpty()) {
            return context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        }else{
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return listProjects.size();
    }
    public static class AdapterProjectsListHolder extends RecyclerView.ViewHolder{
        TextView titleProject;
        ImageView iconProject;
        public AdapterProjectsListHolder(@NonNull View itemView) {
            super(itemView);
            iconProject=itemView.findViewById(R.id.icon_prj_item);
            titleProject=itemView.findViewById(R.id.title_prj_item);
        }
        public void bind(final Project project, final OnItemClickListener listener) {
            titleProject.setText(project.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(project.getId());
                }
            });
        }
    }
}

package com.vn.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.appdesign.R;
import com.vn.models.Project;

import java.util.List;

public class AdapterProjectsListToHome extends RecyclerView.Adapter<AdapterProjectsListToHome.AdapterProjectsListHolder> {
    Context context;
    List<Project> listProjects;
    OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String projectId);
    }

    public AdapterProjectsListToHome(Context context, List<Project> listProjects, OnItemClickListener listener) {
        this.context = context;
        this.listProjects = listProjects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterProjectsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project_recent, parent, false);
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
            iconProject=itemView.findViewById(R.id.icon_issue_item);
            titleProject=itemView.findViewById(R.id.title_issue_item);
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

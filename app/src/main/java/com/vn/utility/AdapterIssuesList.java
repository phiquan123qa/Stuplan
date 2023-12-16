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
import com.vn.models.Issue;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class AdapterIssuesList extends RecyclerView.Adapter<AdapterIssuesList.AdapterIssuesListHolder> {
    Context context;
    List<Issue> listIssues;

    public AdapterIssuesList(Context context, List<Issue> listIssues) {
        this.context = context;
        this.listIssues = listIssues;
    }

    @NonNull
    @Override
    public AdapterIssuesListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_issue, parent, false);
        return new AdapterIssuesListHolder(view);
    }
//Những gì hiển thị ở trên màn hình sẽ config ở onBindViewHolder()
    @Override
    public void onBindViewHolder(@NonNull AdapterIssuesListHolder holder, int position) {
        Issue issue = listIssues.get(position);
        holder.titleIssue.setText(issue.getTitle());
        int iconResourceId = getIconResourceId(context, issue.getIcon());
        if (iconResourceId != 0) {
            holder.iconIssue.setImageResource(iconResourceId);
        }
        Date createDate = issue.getDateCreate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(createDate);

        holder.createTimeIssue.setText(formattedDate);

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
        return listIssues.size();
    }

    public static class AdapterIssuesListHolder extends RecyclerView.ViewHolder{
        TextView titleIssue;
        TextView createTimeIssue;
        ImageView iconIssue;

        public AdapterIssuesListHolder(@NonNull View itemView) {
            super(itemView);
            iconIssue=itemView.findViewById(R.id.icon_issue_item);
            createTimeIssue = itemView.findViewById(R.id.time_issue_item);
            titleIssue=itemView.findViewById(R.id.title_issue_item);
        }
    }
}

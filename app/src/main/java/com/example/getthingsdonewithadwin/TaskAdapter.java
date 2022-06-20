package com.example.getthingsdonewithadwin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    Context context;
    ArrayList<TaskModel> taskArrayList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("yy-MM-dd", Locale.US);
    Date date = null;
    String outputDateString = null;


    public TaskAdapter(Context context, ArrayList<TaskModel> taskArrayList) {
        this.context = context;
        this.taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {


        TaskModel taskModel= taskArrayList.get(position);
        holder.title.setText(taskModel.getTaskTitle());
        holder.description.setText(taskModel.getTaskDesc());
        holder.time.setText(taskModel.getTaskTime());
        try {
            date = inputDateFormat.parse(taskModel.getTaskDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];
            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView day, date, month, title, description, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.day);
            date=itemView.findViewById(R.id.date);
            month=itemView.findViewById(R.id.month);
            title=itemView.findViewById(R.id.title);
            description= itemView.findViewById(R.id.description);
            time=itemView.findViewById(R.id.time);
        }
    }
}

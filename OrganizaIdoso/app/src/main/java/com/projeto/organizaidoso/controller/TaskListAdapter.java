package com.projeto.organizaidoso.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.projeto.organizaidoso.R;
import com.projeto.organizaidoso.model.Task;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private ArrayList<Task> taskList;
    private Context context;
    private MediaPlayer notificationSound;

    public TaskListAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.notificationSound = MediaPlayer.create(context, R.raw.notification);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
        holder.dueDateTextView.setText(dateFormat.format(task.getDueDate()));

        checkDueDates();

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTaskFromSharedPreferences(task);
                taskList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView dueDateTextView;
        public ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            dueDateTextView = itemView.findViewById(R.id.due_date_text_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    private void removeTaskFromSharedPreferences(Task task) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("taskList", Context.MODE_PRIVATE);

        Map<String, ?> allTasks = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allTasks.entrySet()) {
            String key = entry.getKey();
            String taskJson = (String) entry.getValue();

            Task storedTask = new Gson().fromJson(taskJson, Task.class);

            if (storedTask.getTitle().equals(task.getTitle())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
                break;
            }
        }
    }

    private void checkDueDates() {
        long currentTime = System.currentTimeMillis();
        long notificationTime = 24 * 60 * 60 * 1000;

        for (Task task : taskList) {
            long dueTime = task.getDueDate().getTime();
            long timeDiff = dueTime - currentTime;

            if (timeDiff > 0 && timeDiff <= notificationTime) {
                playNotificationSound();
                break;
            }
        }
    }

    private void playNotificationSound() {
        notificationSound.start();
    }
}
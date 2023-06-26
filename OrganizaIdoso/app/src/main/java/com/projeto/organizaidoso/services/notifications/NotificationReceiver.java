//package com.projeto.organizaidoso.services.notifications;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.projeto.organizaidoso.R;
//import com.projeto.organizaidoso.services.tasks.Task;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public class NotificationReceiver extends BroadcastReceiver {
//    private static final int NOTIFICATION_ID = 1;
//    private static final String CHANNEL_ID = "my_channel_id";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedEnum.SHARED_TASKS.getKey(), Context.MODE_PRIVATE);
//
//        List<Task> tasks = getTasksNearDueDate(sharedPreferences);
//
//        for (Task task : tasks) {
//        }
//    }
//
//    private List<Task> getTasksNearDueDate(SharedPreferences sharedPreferences) {
//        List<Task> tasks = new ArrayList<>();
//
//        Map<String, ?> allEntries = sharedPreferences.getAll();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            String taskKey = entry.getKey();
//            String taskValue = (String) entry.getValue();
//
//            String[] taskValues = taskValue.split(",");
//            String taskTitle = taskValues[0];
//            String taskDescription = taskValues[1];
//            String taskDueDate = taskValues[2];
//
//            Date currentDate = new Date();
//            Date dueDate = new Date(Long.parseLong(taskDueDate));
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(currentDate);
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//            Date nextDay = calendar.getTime();
//
//            if (dueDate.before(nextDay)) {
//                Task task = new Task(taskTitle, taskDescription, dueDate);
//                tasks.add(task);
//            }
//        }
//
//        return tasks;
//    }
//}
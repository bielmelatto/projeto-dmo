package com.projeto.organizaidoso.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.projeto.organizaidoso.R;
import com.projeto.organizaidoso.databinding.FragmentHomeBinding;
import com.projeto.organizaidoso.enums.SharedEnum;
import com.projeto.organizaidoso.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeView extends Fragment {
    private ArrayList<Task> taskList = new ArrayList<>();
    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePicker dueDatePicker;
    private Button saveButton;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        titleEditText = view.findViewById(R.id.title_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        dueDatePicker = view.findViewById(R.id.due_date_picker);
        saveButton = view.findViewById(R.id.save_button);

        Button selectDateButton = view.findViewById(R.id.select_date_button);
        DatePicker dueDatePicker = view.findViewById(R.id.due_date_picker);
        Button selectTimeButton = view.findViewById(R.id.select_time_button);
        TimePicker timePicker = view.findViewById(R.id.due_time_picker);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = dueDatePicker.getYear();
                int month = dueDatePicker.getMonth();
                int day = dueDatePicker.getDayOfMonth();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date date = calendar.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
                        selectDateButton.setText(dateFormat.format(date));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        dueDatePicker.init(dueDatePicker.getYear(), dueDatePicker.getMonth(), dueDatePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectDateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                dueDatePicker.setVisibility(View.GONE);
            }
        });

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePicker.setHour(hourOfDay);
                        timePicker.setMinute(minute);
                        selectTimeButton.setText(hourOfDay+":"+minute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedEnum.SHARED_TASKS.getKey(), Context.MODE_PRIVATE);
        String taskListString = sharedPreferences.getString("taskList", "");
        if (!taskListString.isEmpty()) {
            try {
                JSONArray taskListArray = new JSONArray(taskListString);
                for (int i = 0; i < taskListArray.length(); i++) {
                    JSONObject taskObject = taskListArray.getJSONObject(i);
                    String title = taskObject.getString("title");
                    String description = taskObject.getString("description");
                    String dueDateString = taskObject.getString("dueDate");
                    Date dueDate = new SimpleDateFormat("yyyy-M-dd").parse(dueDateString);
                    Task task = new Task(title, description, dueDate);
                    taskList.add(task);
                }
            } catch (JSONException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            int day = dueDatePicker.getDayOfMonth();
            int month = dueDatePicker.getMonth();
            int year = dueDatePicker.getYear();

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);
            Date dueDate = calendar.getTime();

            Task task = new Task(title, description, dueDate);

            saveTask(task);

            JSONArray taskListArray = new JSONArray();
            for (Task t : taskList) {
                JSONObject taskObject = new JSONObject();
                try {
                    taskObject.put("title", t.getTitle());
                    taskObject.put("description", t.getDescription());
                    taskObject.put("dueDate", new SimpleDateFormat("yyyy-M-dd").format(t.getDueDate()));
                    taskListArray.put(taskObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SharedEnum.SHARED_TASKS.getKey(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.clear();
            editor.putString("taskList", taskListArray.toString());
            editor.apply();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.save_task);
            builder.setMessage(R.string.save_task_success);
            builder.setPositiveButton("OK", (dialog, which) -> {
                titleEditText.setText("");
                descriptionEditText.setText("");
                Calendar calendar1 = Calendar.getInstance();
                dueDatePicker.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long tomorrow = calendar.getTimeInMillis();

        return view;
    }

    public void saveTask(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            Task existingTask = taskList.get(i);
            if (existingTask.getTitle().equals(task.getTitle()) && isSameDay(existingTask.getDueDate(), task.getDueDate())) {
                existingTask.setDescription(task.getDescription());
                existingTask.setDueDate(task.getDueDate());
                return;
            }
        }
        taskList.add(task);
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
package com.projeto.organizaidoso.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projeto.organizaidoso.R;
import com.projeto.organizaidoso.databinding.FragmentDashboardBinding;
import com.projeto.organizaidoso.enums.SharedEnum;
import com.projeto.organizaidoso.services.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.task_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedEnum.SHARED_TASKS.getKey(), Context.MODE_PRIVATE);

        ArrayList<Task> taskList = new ArrayList<>();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String taskString = entry.getValue().toString();

            try {
                JSONArray taskArray = new JSONArray(taskString);
                for (int i = 0; i < taskArray.length(); i++) {
                    JSONObject taskObject = taskArray.getJSONObject(i);
                    String title = taskObject.getString("title");
                    String description = taskObject.getString("description");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
                    Date dueDate = dateFormat.parse(taskObject.getString("dueDate"));

                    taskList.add(new Task(title, description, dueDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        TaskListAdapter adapter = new TaskListAdapter(getContext(), taskList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
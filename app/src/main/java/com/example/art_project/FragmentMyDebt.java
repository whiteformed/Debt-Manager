package com.example.art_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentMyDebt extends Fragment implements OnDataUpdateListener, OnRecyclerViewItemClickListener {
    View view;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    SqlDatabaseHelper sqlDatabaseHelper;
    ArrayList<Person> personArrayList = new ArrayList<>();
    int status = 0;

    private String tablePersons = SqlDatabaseHelper.getPersonsTableName();

    @Override
    public void onItemClick(int id) {
        Toaster.makeToast(getNonNullActivity(), String.valueOf(id));
    }

    private FragmentActivity getNonNullActivity() {
        if (super.getActivity() != null) {
            return super.getActivity();
        } else {
            throw new RuntimeException("Null returned from getActivity() method");
        }
    }

    @Override
    public void onDataUpdate(Person person) {
        boolean result = sqlDatabaseHelper.addPerson(person);
        updateList();
    }

    private void updateList() {
        AsynchronousTask task = new AsynchronousTask(recyclerViewAdapter, sqlDatabaseHelper, tablePersons, personArrayList, status);
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person_list, container, false);

        DialogHelper dialogHelper = ((ActivityMain) getNonNullActivity()).getDialogHelper();
        dialogHelper.setListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sqlDatabaseHelper = new SqlDatabaseHelper(getActivity());
        personArrayList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), personArrayList, status);
        recyclerViewAdapter.setListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        updateList();

        return view;
    }
}

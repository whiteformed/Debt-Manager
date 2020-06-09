package com.example.art_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentTheirDebt extends Fragment {
    View view;

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    PersonListAdapter personArrayListAdapter;
    ArrayList<Person> personArrayList = new ArrayList<>();
    OnPersonItemClickListener onPersonItemClickListener;
    int status;

    TextView tv_msg_empty_list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void updatePersonArrayList() {
//        AsynchronousTask task = new AsynchronousTask(new SqlDatabaseHelper(getActivity()), personArrayListAdapter, personArrayList, status);
//        task.execute();

        personArrayList.clear();
        personArrayList.addAll(new SqlDatabaseHelper(getActivity()).getPersonArrayList(status));
        personArrayListAdapter.notifyDataSetChanged();

        int visibility = personArrayList.isEmpty() ? View.VISIBLE : View.INVISIBLE;
        tv_msg_empty_list.setVisibility(visibility);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person_list, container, false);

        tv_msg_empty_list = view.findViewById(R.id.fragment_person_list_tv_msg_empty_list);

        rv = view.findViewById(R.id.fragment_person_list_rv);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        personArrayListAdapter = new PersonListAdapter(getActivity(), personArrayList, status, onPersonItemClickListener);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(personArrayListAdapter);

        updatePersonArrayList();

        return view;
    }
}

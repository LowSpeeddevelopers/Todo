package com.super15.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.super15.todo.Adapter.ExpandableListViewAdapter;
import com.super15.todo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    private ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;

    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initViews();
        initListeners();
        initObjects();
        initListData();

    }

    private void initViews() {

        expandableListView = findViewById(R.id.expandableListView);

    }

    private void initListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

    }
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);

    }
    private void initListData() {
        // Adding group data
        listDataGroup.add(getString(R.string.text_on_off));
        listDataGroup.add(getString(R.string.text_update_delete));
        listDataGroup.add(getString(R.string.text_priority));
        listDataGroup.add(getString(R.string.text_add_remainder));
        listDataGroup.add(getString(R.string.text_transfer));
        listDataGroup.add(getString(R.string.text_question));
        String[] array;
        List<String> onList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_on_off);
        for (String item : array) {
            onList.add(item);
        }
        List<String> updateList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_update_delete);
        for (String item : array) {
            updateList.add(item);
        }
        List<String> priorityList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_priority);
        for (String item : array) {
            priorityList.add(item);
        }
        List<String> remainderList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_remainder);
        for (String item : array) {
            remainderList.add(item);
        }
        List<String> transferList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_transfer);
        for (String item : array) {
            transferList.add(item);
        }
        List<String> questionList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_question);
        for (String item : array) {
            questionList.add(item);
        }
        // Adding child data
        listDataChild.put(listDataGroup.get(0), onList);
        listDataChild.put(listDataGroup.get(1), updateList);
        listDataChild.put(listDataGroup.get(2), priorityList);
        listDataChild.put(listDataGroup.get(3), remainderList);
        listDataChild.put(listDataGroup.get(4), transferList);
        listDataChild.put(listDataGroup.get(5), questionList);
        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }

}

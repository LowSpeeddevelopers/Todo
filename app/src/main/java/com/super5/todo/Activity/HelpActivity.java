package com.super5.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.ClipData;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import com.super5.todo.Adapter.ExpandableListViewAdapter;
import com.super5.todo.R;
import java.util.ArrayList;
import java.util.Arrays;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        initViews();
        initListeners();
        initObjects();
        initListData();

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        listDataGroup.add(getString(R.string.text_add_remainder));
        listDataGroup.add(getString(R.string.text_priority));
        listDataGroup.add(getString(R.string.text_update_delete));
        listDataGroup.add(getString(R.string.text_transfer));
        listDataGroup.add(getString(R.string.text_question));
        String[] array;
        array = getResources().getStringArray(R.array.string_array_on_off);
        List<String> onList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_remainder);
        List<String> updateList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_priority);
        List<String> priorityList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_update_delete);
        List<String> remainderList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_transfer);
        List<String> transferList = new ArrayList<>(Arrays.asList(array));
        array = getResources().getStringArray(R.array.string_array_question);
        List<String> questionList = new ArrayList<>(Arrays.asList(array));
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

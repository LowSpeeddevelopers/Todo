package com.super5.todo.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
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
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initViews() {
        expandableListView = findViewById(R.id.expandableListView);
    }
    private void initListeners() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
    }
    private void initObjects() {
        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();
        expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild);
        expandableListView.setAdapter(expandableListViewAdapter);

    }
    private void initListData() {
        // Adding group data
        listDataGroup.add(getString(R.string.text_on_off));
        listDataGroup.add(getString(R.string.text_add_remainder));
        listDataGroup.add(getString(R.string.text_priority));
        listDataGroup.add(getString(R.string.text_update_delete));
        listDataGroup.add(getString(R.string.text_transfer));
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
        List<String> questionList = new ArrayList<>(Arrays.asList(array));
        listDataChild.put(listDataGroup.get(0), onList);
        listDataChild.put(listDataGroup.get(1), updateList);
        listDataChild.put(listDataGroup.get(2), priorityList);
        listDataChild.put(listDataGroup.get(3), remainderList);
        listDataChild.put(listDataGroup.get(4), transferList);
        expandableListViewAdapter.notifyDataSetChanged();
    }
}

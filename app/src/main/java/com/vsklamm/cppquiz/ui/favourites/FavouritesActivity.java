package com.vsklamm.cppquiz.ui.favourites;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.utils.ActivityUtils;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;

import com.vsklamm.cppquiz.data.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;


public class FavouritesActivity extends AppCompatActivity {
    private SharedPreferences appPreferences;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ActivityUtils.setUpTheme(this, appPreferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LinkedHashSet<Integer> favouriteQuestions = UserData.getInstance().getFavouriteQuestions();
        ArrayList<Integer> list_ids = new ArrayList<>();
        list_ids.addAll(favouriteQuestions);
        Collections.sort(list_ids);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(list_ids);
        mRecyclerView.setAdapter(mAdapter);
    }
/*

        final ListView listview = (ListView) findViewById(R.id.listview);
        LinkedHashSet<Integer> favouriteQuestions = UserData.getInstance().getFavouriteQuestions();
        SortedSet<Integer> ids = new TreeSet<>();
        ids.addAll(favouriteQuestions);
        final ArrayList<String> list = new ArrayList<>();
        for (Integer id : ids) {
            list.add("Question #" + id.toString());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

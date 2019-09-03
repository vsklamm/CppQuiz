package com.vsklamm.cppquiz.ui.favourites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.utils.ActivityUtils;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;

import com.vsklamm.cppquiz.data.model.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import static com.vsklamm.cppquiz.ui.main.MainActivity.QUESTION;
import static com.vsklamm.cppquiz.ui.main.MainActivity.THEME;

public class FavouritesActivity extends AppCompatActivity {

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
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LinkedHashSet<Integer> favouriteQuestions = UserData.getInstance().getFavouriteQuestions();

        final ArrayList<Integer> listIds = new ArrayList<>();
        listIds.addAll(favouriteQuestions);

        Collections.sort(listIds);

        String codeTheme = appPreferences.getString(THEME, "GITHUB");
        mAdapter = new MyAdapter(listIds, codeTheme, position -> {
            Intent intent = new Intent();
            intent.putExtra(QUESTION, listIds.get(position));
            setResult(RESULT_FIRST_USER, intent);
            finish();
        });
        mRecyclerView.setAdapter(mAdapter);
    }

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

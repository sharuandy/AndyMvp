package com.andy.andymvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andy.andymvp.fragment.RecyclerFragment;

public class DashBoardActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG = "RECYCLER_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        if (savedInstanceState == null) {
            RecyclerFragment recyclerFragment = new RecyclerFragment();
            recyclerFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, recyclerFragment, FRAGMENT_TAG).commit();
            Log.e("TAG", "savedInstanceState == null");
        } else {
            Log.e("TAG", "savedInstanceState != null");
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}

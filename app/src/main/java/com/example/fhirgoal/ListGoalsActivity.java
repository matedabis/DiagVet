package com.example.fhirgoal;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ListGoalsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ListGoalsActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseUser user;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Goal> mItemsData;
    private ListGoalsAdapter mAdapter;

    private SharedPreferences preferences;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);

        setTitle("Goal tracker - Goals");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

/*        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        if(preferences != null) {
            cartItems = preferences.getInt("cartItems", 0);
            gridNumber = preferences.getInt("gridNum", 1);
        }*/

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ListGoalsAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        // Get the data.
        initializeData();
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] goalsList = getResources()
                .getStringArray(R.array.goal_texts);
        String[] goalsDesc = getResources()
                .getStringArray(R.array.goal_desc);
        String[] goalsStatus = getResources()
                .getStringArray(R.array.goal_status);
        String[] goalsDue =
                getResources().getStringArray(R.array.goal_due);
        String[] goalsCategory = getResources().getStringArray(R.array.goal_category);

        // Clear the existing data (to avoid duplication).
        mItemsData.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < goalsList.length; i++) {
            mItemsData.add(new Goal(goalsList[i], goalsCategory[i], goalsStatus[i], goalsDesc[i],
                    goalsDue[i]));
        }
        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_bar);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.d(LOG_TAG, s);
//                mAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.log_out_button:
//                Log.d(LOG_TAG, "Logout clicked!");
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                return true;
//            case R.id.settings_button:
//                Log.d(LOG_TAG, "Setting clicked!");
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                return true;
//            case R.id.cart:
//                Log.d(LOG_TAG, "Cart clicked!");
//                return true;
//            case R.id.view_selector:
//                if (viewRow) {
//                    changeSpanCount(item, R.drawable.ic_view_grid, 1);
//                } else {
//                    changeSpanCount(item, R.drawable.ic_view_row, 2);
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
//        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
//
//        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
//        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
//
//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(alertMenuItem);
//            }
//        });
//        return super.onPrepareOptionsMenu(menu);
//    }
//
    public void updateAlertIcon() {
        cartItems = (cartItems + 1);
        if (0 < cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);
    }

/*    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cartItems", cartItems);
        editor.putInt("gridNum", gridNumber);
        editor.apply();

        Log.i(LOG_TAG, "onPause");
    }*/
}
package com.example.fhirgoal;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ListGoalsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ListGoalsActivity.class.getName();
    private static final int SECRET_KEY = 99;
    private FirebaseUser user;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private int gridNumber = 1;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Goal> mItemsData;
    private ListGoalsAdapter mAdapter;

    private NotificationHelper mNotificationHelper;

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

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");

        queryData();

        mNotificationHelper = new NotificationHelper(this);

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ListGoalsAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        // Get the data.
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
            mItems.add(new Goal(goalsList[i], goalsCategory[i], goalsStatus[i], goalsDesc[i],
                    goalsDue[i]));
        }
        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }


    private void queryData() {
        mItemsData.clear();
        mItems.orderBy("text").limit(100).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Goal item = document.toObject(Goal.class);
                        item.setId(document.getId());
                        mItemsData.add(item);
                    }

                    if (mItemsData.size() == 0) {
                        initializeData();
                        queryData();
                    }

                    // Notify the adapter of the change.
                    mAdapter.notifyDataSetChanged();
                });
    }

    public void deleteItem(Goal item) {
        DocumentReference ref = mItems.document(item.getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item.getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item.getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        queryData();
        mNotificationHelper.send("Item is successfully deleted");
    }

    public void modifyItem(Goal item) {
        Intent intent = new Intent(this, ModifyActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        intent.putExtra("item", item.getId());
        intent.putExtra("text", item.getText());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("lifecycleStatus", item.getLifecycleStatus());
        intent.putExtra("category", item.getCategory());
        intent.putExtra("due", item.getDueDate());
        startActivity(intent);
    }

    public void newItem(View view) {
        Intent intent = new Intent(this, NewItemActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void goBack(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotificationHelper.cancel();
        Log.i(LOG_TAG, "onResume");
    }

}
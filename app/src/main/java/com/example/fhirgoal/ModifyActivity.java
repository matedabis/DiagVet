package com.example.fhirgoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModifyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = ModifyActivity.class.getName();
    private static final String PREF_KEY = ModifyActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText titleEditText;
    EditText detailEditText;
    EditText statusEditText;
    EditText categoryEditText;
    EditText dueEditText;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        setTitle("DiagVet - módosítás");

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY");
        itemId = bundle.getString("item");

        if (secret_key != 99) {
            finish();
        }

        titleEditText = findViewById(R.id.titleEditText);
        detailEditText = findViewById(R.id.detailEditText);
        statusEditText = findViewById(R.id.statusEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        dueEditText = findViewById(R.id.dueEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        titleEditText.setText(bundle.getString("text"));
        detailEditText.setText(bundle.getString("description"));
        statusEditText.setText(bundle.getString("lifecycleStatus"));
        categoryEditText.setText(bundle.getString("category"));
        dueEditText.setText(bundle.getString("due"));

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    public void modify(View view) {
        String title = titleEditText.getText().toString();
        String detail = detailEditText.getText().toString();
        String status = statusEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String due = dueEditText.getText().toString();

        if (title.isEmpty() || detail.isEmpty() || status.isEmpty() || category.isEmpty() || due.isEmpty()) {
            Log.e(LOG_TAG, "Minden mező kitöltése kötelező");
            Toast.makeText(ModifyActivity.this, "Modify fail: missing user data", Toast.LENGTH_LONG).show();
            return;
        }
        mFirestore = FirebaseFirestore.getInstance();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("text",title);
        map.put("description", detail);
        map.put("lifecycleStatus", status);
        map.put("category", category);
        map.put("dueDate", due);

        mFirestore.collection("Items").document(itemId).update(map);

        startTracker();

    }



    public void cancel(View view) {
        finish();
    }

    private void startTracker() {
        Intent intent = new Intent(this, ListIllnessesAdapter.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
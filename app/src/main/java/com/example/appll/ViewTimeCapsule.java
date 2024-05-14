package com.example.appll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewTimeCapsule extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private TimeCapsuleAdapter adapter;
    private ArrayList<MessageData> messageList;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_capsule);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ArrayList to store fetched data
        messageList = new ArrayList<>();

        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference("timeCapsules");

        // Initialize adapter
        adapter = new TimeCapsuleAdapter(ViewTimeCapsule.this, messageList);
        recyclerView.setAdapter(adapter);

        // Fetch and display data
        fetchDataFromFirebase();
    }
    private void fetchDataFromFirebase() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageData messageData = dataSnapshot.getValue(MessageData.class);
                    if (messageData != null && userId != null && userId.equals(messageData.getUserId())) {
                        // Check if the current device time has passed the stored datetime
                        if (isPastDateTime(messageData.getDateTime())) {
                            // Fetch image URL along with other data
                            String imageUrl = messageData.getImageUri();
                            messageData.setImageUri(imageUrl);
                            messageList.add(messageData);
                        }
                    }
                }
                // Notify adapter of data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Toast.makeText(ViewTimeCapsule.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to check if the stored datetime is in the past
    private boolean isPastDateTime(String dateTimeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date storedDateTime = dateFormat.parse(dateTimeString);
            Date currentDateTime = new Date(); // Get current device time
            return currentDateTime.after(storedDateTime);
        } catch (ParseException e) {
            Log.e("ViewTimeCapsule", "Error parsing datetime: " + e.getMessage());
            return false; // Return false in case of an error
        }
    }
}


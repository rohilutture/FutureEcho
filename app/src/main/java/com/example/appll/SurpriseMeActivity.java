package com.example.appll;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SurpriseMeActivity extends AppCompatActivity {

    private TextView tvMessage,tvTitle;
    private Button btnSurpriseMe;
    private ImageView imageView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_me);

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvMessage = findViewById(R.id.tv_message);
        btnSurpriseMe = findViewById(R.id.btn_surprise_me);
        imageView = findViewById(R.id.imageView); // Initialize imageView

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("timeCapsules");

        // Set button click listener
        btnSurpriseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomMessage();
            }
        });
    }
    private void displayRandomMessage() {
        // Get the ID of the current user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query to get the time capsules of the current user
        Query query = databaseReference.orderByChild("userId").equalTo(userId);

        // Execute the query
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if there are any time capsules for the current user
                if (snapshot.exists()) {
                    // Count the total number of time capsules
                    long timeCapsuleCount = snapshot.getChildrenCount();

                    // Generate a random index to select a time capsule
                    int randomIndex = (int) (Math.random() * timeCapsuleCount);

                    // Counter to track the index
                    int currentIndex = 0;

                    // Iterate through the time capsules to find the randomly selected one
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Check if the current index matches the random index
                        if (currentIndex == randomIndex) {
                            // Get the time capsule data
                            MessageData messageData = dataSnapshot.getValue(MessageData.class);
                            if (messageData != null) {
                                // Display the title, image, and message
                                String title = messageData.getTitle();
                                String message = messageData.getMessage();
                                String imageUri = messageData.getImageUri(); // Assuming you have this method

                                // Update the UI with the retrieved data
                                tvTitle.setText(title);
                                tvMessage.setText(message);

                                // Use Glide or another image loading library to load the image
                                Glide.with(SurpriseMeActivity.this)
                                        .load(messageData.getImageUri())
                                        .into(imageView);
                            }
                            return; // Exit the loop after finding the random time capsule
                        }
                        currentIndex++; // Increment the index counter
                    }
                } else {
                    // No time capsules found for the current user
                    Toast.makeText(SurpriseMeActivity.this, "No time capsules found for the current user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Toast.makeText(SurpriseMeActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void displayRandomMessage() {
//        // Query to get all time capsules from the database
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Check if there are any time capsules
//                if (snapshot.exists()) {
//                    // Count the total number of time capsules
//                    long timeCapsuleCount = snapshot.getChildrenCount();
//
//                    // Generate a random index to select a time capsule
//                    int randomIndex = (int) (Math.random() * timeCapsuleCount);
//
//                    // Counter to track the index
//                    int currentIndex = 0;
//
//                    // Iterate through the time capsules to find the randomly selected one
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        // Check if the current index matches the random index
//                        if (currentIndex == randomIndex) {
//                            // Get the time capsule data
//                            MessageData messageData = dataSnapshot.getValue(MessageData.class);
//                            if (messageData != null) {
//                                // Display the title, image, and message
//                                String title = messageData.getTitle();
//                                String message = messageData.getMessage();
//                                String imageUri = messageData.getImageUri(); // Assuming you have this method
//
//                                // Update the UI with the retrieved data
//                                tvTitle.setText(title);
//                                tvMessage.setText(message);
//
//                                // Use Glide or another image loading library to load the image
//                                Glide.with(SurpriseMeActivity.this)
//                                        .load(messageData.getImageUri())
//                                        .into(imageView);
//                            }
//                            return; // Exit the loop after finding the random time capsule
//                        }
//                        currentIndex++; // Increment the index counter
//                    }
//                } else {
//                    // No time capsules found
//                    Toast.makeText(SurpriseMeActivity.this, "No time capsules found.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors
//                Toast.makeText(SurpriseMeActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


//    private void displayRandomMessage() {
//        // Query to get all messages from the database
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Check if there are any messages
//                if (snapshot.exists()) {
//                    // Count the total number of messages
//                    long messageCount = snapshot.getChildrenCount();
//
//                    // Generate a random index to select a message
//                    int randomIndex = (int) (Math.random() * messageCount);
//
//                    // Counter to track the index
//                    int currentIndex = 0;
//
//                    // Iterate through the messages to find the randomly selected one
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        // Check if the current index matches the random index
//                        if (currentIndex == randomIndex) {
//                            // Get the message and display it
//                            MessageData messageData = dataSnapshot.getValue(MessageData.class);
//                            if (messageData != null) {
//                                String message = messageData.getMessage();
//                                tvMessage.setText(message);
//                            }
//                            return; // Exit the loop after finding the random message
//                        }
//                        currentIndex++; // Increment the index counter
//                    }
//                } else {
//                    // No message found
//                    Toast.makeText(SurpriseMeActivity.this, "No time capsule message found.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors
//                Toast.makeText(SurpriseMeActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


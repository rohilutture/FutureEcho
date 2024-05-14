package com.example.appll;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DigitalLegacyPlanning extends AppCompatActivity {

    private EditText editYourName, editRecipientName;
    private Button btnUpdate;
    private TextView textLegacyInfo;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_legacy_planning);

        editYourName = findViewById(R.id.edit_your_name);
        editRecipientName = findViewById(R.id.edit_recipient_name);
        btnUpdate = findViewById(R.id.btn_update);
        textLegacyInfo = findViewById(R.id.text_legacy_info);

        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("willInfo");

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String yourName = editYourName.getText().toString().trim();
                String recipientName = editRecipientName.getText().toString().trim();

                if (yourName.isEmpty() || recipientName.isEmpty()) {
                    Toast.makeText(DigitalLegacyPlanning.this, "Please enter both names", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the will information to Firebase
                    saveWillInfo(yourName, recipientName);
                }
            }
        });
    }
    private void saveWillInfo(String yourName, String recipientName) {
        // Get current user ID
        String yourId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // You need to set recipientId based on how you retrieve the recipient's user ID
        String recipientId = "recipientUserId";

        // Create a WillInfo object
        WillInfo willInfo = new WillInfo(yourId, yourName, recipientId, recipientName);

        // Push the WillInfo object to Firebase Realtime Database
        databaseReference.setValue(willInfo);

        // Update the legacy information text
        String legacyInfo = "I, " + yourName + ", entrust my time capsules to " + recipientName + ". " +
                "Upon my death, he/she shall receive and safeguard the capsules for future generations. " +
                "FutureECHO is appointed as Executor to oversee this transfer.";
        textLegacyInfo.setText(legacyInfo);

        // Display success message
        Toast.makeText(this, "Will information saved successfully", Toast.LENGTH_SHORT).show();
    }

//    private void saveWillInfo(String yourName, String recipientName) {
//        // Get current user ID
//        String yourId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // You need to set recipientId based on how you retrieve the recipient's user ID
//        String recipientId = "recipientUserId";
//
//        // Create a WillInfo object
//        WillInfo willInfo = new WillInfo(yourId, yourName, recipientId, recipientName);
//
//        // Push the WillInfo object to Firebase Realtime Database
//        databaseReference.setValue(willInfo);
//
//        // Display success message
//        Toast.makeText(this, "Will information saved successfully", Toast.LENGTH_SHORT).show();
//    }
}

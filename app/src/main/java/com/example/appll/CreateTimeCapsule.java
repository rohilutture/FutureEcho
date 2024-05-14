package com.example.appll;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateTimeCapsule extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private EditText edmessage, edtitle;
    private Button SelectDateTime, btnSelectImage;
    private Button Submit;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private Uri imageUri; // Uri to store the selected or captured image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time_capsule);

        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("timeCapsules");
        edtitle = findViewById(R.id.edtitle);
        edmessage = findViewById(R.id.edmessage);
        SelectDateTime = findViewById(R.id.selectDateTimeButton);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        Submit = findViewById(R.id.submit);
        calendar = Calendar.getInstance();

        SelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeMessageToFirebase();
            }
        });
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showTimePicker();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String selectedDateTime = dateFormat.format(calendar.getTime());
                Toast.makeText(CreateTimeCapsule.this, "Selected Date and Time: " + selectedDateTime, Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    private void selectImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.appll.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured from camera successfully
            // The imageUri already points to the captured image
            // You can display the image preview if needed
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Image selected from gallery successfully
            imageUri = data.getData();
            // You can display the image preview if needed
        }
    }

    private void storeMessageToFirebase() {
        String title = edtitle.getText().toString().trim();
        String message = edmessage.getText().toString().trim();

        if (!title.isEmpty() && !message.isEmpty() && imageUri != null) {
            // Upload image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get selected date and time
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String dateTime = dateFormat.format(calendar.getTime());

                                    // Get userID of the logged-in user
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String userID = user != null ? user.getUid() : null;

                                    if (userID != null) {
                                        // Create message object with title, message, image URL, and userID
                                        MessageData messageData = new MessageData(title, message, uri.toString(), dateTime, userID);

                                        // Push message data to Firebase Realtime Database
                                        String key = databaseReference.push().getKey();
                                        if (key != null) {
                                            databaseReference.child(key).setValue(messageData);
                                        }

                                        // Clear message EditText
                                        edmessage.setText("");

                                        // Show success message
                                        Toast.makeText(CreateTimeCapsule.this, "Message stored successfully!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(CreateTimeCapsule.this, SecondActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Show error message if userID is null
                                        Toast.makeText(CreateTimeCapsule.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while uploading image
                            e.printStackTrace();
                            Toast.makeText(CreateTimeCapsule.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Show error message if title, message, or image is empty
            Toast.makeText(this, "Please enter a title, a message, and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    // MessageData class remains the same
    public class MessageData {
        private String title;
        private String message;
        private String imageUri; // URL to store the image
        private String dateTime;
        private String userId;

        // Constructors
        public MessageData() {
            // Default constructor required for calls to DataSnapshot.getValue(MessageData.class)
        }

        public MessageData(String title, String message, String imageUri, String dateTime, String userId) {
            this.title = title;
            this.message = message;
            this.imageUri = imageUri;
            this.dateTime = dateTime;
            this.userId = userId;
        }

        // Getter methods
        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getImageUri() {
            return imageUri;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getUserId() {
            return userId;
        }
    }
}
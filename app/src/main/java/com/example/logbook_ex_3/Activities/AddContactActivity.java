package com.example.logbook_ex_3.Activities;

import android.os.Bundle;
import android.content.Intent;

import androidx.room.Room;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;
import com.example.logbook_ex_3.Database.AppDatabase;

public class AddContactActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private String selectedImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        for (int i = 1; i <= 3; i++) {
            int imageId = getResources().getIdentifier("image" + i, "id", getPackageName());
            int radioId = getResources().getIdentifier("radio" + i, "id", getPackageName());
            ImageView image = findViewById(imageId);
            String imageUrl = "@drawable/image" + i;
            int finalI = i;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleImageClick(imageUrl, finalI);
                }
            });
            RadioButton radio = findViewById(radioId);
            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleImageClick(imageUrl, finalI);
                }
            });
        }
        RadioButton radio = findViewById(R.id.radio1);
        radio.callOnClick();
        radio.setChecked(true);
    }

    private void handleImageClick(String imageUrl, int id) {
        if (imageUrl != null) {
            selectedImageUrl = imageUrl;
        }
        // turn off all other radio buttons
        for (int i = 1; i <= 3; i++) {
            int radioId = getResources().getIdentifier("radio" + i, "id", getPackageName());
            RadioButton radio = findViewById(radioId);
            if (i != id) {
                radio.setChecked(false);
            }
        }

    }

    private void cancel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveDetails() {
        if (selectedImageUrl == null || selectedImageUrl.isEmpty() || selectedImageUrl.length() < 2) {
            Toast.makeText(this, "Please select an image",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        // Get references to the EditText views and read their content
        EditText nameTxt = findViewById(R.id.nameText);
        EditText dobTxt = findViewById(R.id.dobText);
        EditText emailTxt = findViewById(R.id.emailText);

        String name = nameTxt.getText().toString();
        String dob = dobTxt.getText().toString();
        String email = emailTxt.getText().toString();

        Person person = new Person();
        person.name = name;
        person.dob = dob;
        person.email = email;
        person.url_avatar = selectedImageUrl;

        // Calls the insertDetails method we created
        long personId = appDatabase.personDao().insertPerson(person);

        // Shows a toast with the automatically generated id
        Toast.makeText(this, "Person has been created with id: " + personId,
                Toast.LENGTH_LONG
        ).show();

        // Launch Details Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
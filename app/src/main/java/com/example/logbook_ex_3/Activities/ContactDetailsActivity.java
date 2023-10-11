package com.example.logbook_ex_3.Activities;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;
import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;
import com.example.logbook_ex_3.Database.AppDatabase;

public class ContactDetailsActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Long personId = Long.parseLong(intent.getStringExtra("person_id"));
        String personName = ((Intent) intent).getStringExtra("person_name");
        String personDob = intent.getStringExtra("person_dob");
        String personEmail = intent.getStringExtra("person_email");
        String personImage = intent.getStringExtra("person_image");

        // Capture the layout's TextView and set the string as its text
        EditText name = findViewById(R.id.nameEditText);
        EditText dob = findViewById(R.id.dobEditText);
        EditText email = findViewById(R.id.emailEditText);

        name.setText(personName);
        dob.setText(personDob);
        email.setText(personEmail);

        // Capture the layout's ImageView and set the image as its image
        ImageView image = findViewById(R.id.avatarImageView);
        int imageId = getResources().getIdentifier(personImage, "drawable", getPackageName());
        image.setImageResource(imageId);

        // Add the listener for the update button
        Button saveDetailsButton = findViewById(R.id.saveButton);

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails(personId, name.getText().toString(), dob.getText().toString(), email.getText().toString(), personImage);
            }
        });

        // Add the listener for the delete button
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDetails(personId);
            }
        });

    }

    public void saveDetails(Long personId, String name, String dob, String email, String avatar) {
        Person person = new Person();
        person.person_id = personId;
        person.name = name;
        person.dob = dob;
        person.email = email;
        person.url_avatar = avatar;
        appDatabase.personDao().updatePerson(person);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteDetails(Long personId) {
        Person person = new Person();
        person.person_id = personId;
        appDatabase.personDao().deletePerson(person);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

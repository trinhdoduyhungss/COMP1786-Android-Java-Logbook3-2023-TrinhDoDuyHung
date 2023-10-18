package com.example.logbook_ex_3.Activities;
import java.time.LocalDate;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.DatePickerDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;
import androidx.fragment.app.DialogFragment;

import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;
import com.example.logbook_ex_3.Database.AppDatabase;

public class ContactDetailsActivity extends AppCompatActivity {
    private AppDatabase appDatabase;

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((ContactDetailsActivity)getActivity()).updateDOB(dob);
        }
    }

    public void updateDOB(LocalDate dob){
        EditText dobControl = findViewById(R.id.dobEditText);
        dobControl.setText(dob.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean validInputs(String name, String email, String dob){
        if(name == null || name.isEmpty()){
            Toast.makeText(this, "Please enter a name",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Please enter an email",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
        else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toast.makeText(this, "Please enter a valid email",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
        if (LocalDate.parse(dob).isAfter(LocalDate.now())) {
            Toast.makeText(this, "Please enter a date of birth in the past",
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
        return true;
    }

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

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new ContactDetailsActivity.DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        dob.setKeyListener(null);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveDetails(Long personId, String name, String dob, String email, String avatar) {
        if(!validInputs(name, email, dob)){
            return;
        }

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

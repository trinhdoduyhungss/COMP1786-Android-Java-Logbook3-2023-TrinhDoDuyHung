package com.example.logbook_ex_3.Activities;
import java.time.LocalDate;

import android.os.Build;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.room.Room;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.app.Dialog;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.app.DatePickerDialog;

import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;
import com.example.logbook_ex_3.Database.AppDatabase;

public class AddContactActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private String selectedImageUrl = "";

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
            ((AddContactActivity)getActivity()).updateDOB(dob);
        }
    }

    public void updateDOB(LocalDate dob){
        EditText dobControl = findViewById(R.id.dobText);
        dobControl.setText(dob.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        EditText dobControl = findViewById(R.id.dobText);
        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        dobControl.setKeyListener(null);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        if(!validInputs(name, email, dob)){
            return;
        }

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
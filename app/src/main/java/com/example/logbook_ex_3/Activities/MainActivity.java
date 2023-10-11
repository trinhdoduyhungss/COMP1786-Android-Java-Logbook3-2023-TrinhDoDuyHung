package com.example.logbook_ex_3.Activities;
import java.util.List;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import androidx.room.Room;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbook_ex_3.Database.AppDatabase;
import com.example.logbook_ex_3.Adapter.ContactAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addContactButton = findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(view);
            }
        });

        // For simplicity, don't use this in production
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Person> persons = appDatabase.personDao().getAllPersons();

        ContactAdapter adapter = new ContactAdapter(persons);
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                Person person = persons.get(position);
                intent.putExtra("person_id", Long.toString(person.person_id));
                intent.putExtra("person_name", person.name);
                intent.putExtra("person_dob", person.dob);
                intent.putExtra("person_email", person.email);
                intent.putExtra("person_image", person.url_avatar);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void addContact(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }
}

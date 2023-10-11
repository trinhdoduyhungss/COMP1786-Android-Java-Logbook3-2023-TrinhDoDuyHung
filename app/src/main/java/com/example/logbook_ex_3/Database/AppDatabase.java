package com.example.logbook_ex_3.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.logbook_ex_3.Dao.PersonDao;
import com.example.logbook_ex_3.Models.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}


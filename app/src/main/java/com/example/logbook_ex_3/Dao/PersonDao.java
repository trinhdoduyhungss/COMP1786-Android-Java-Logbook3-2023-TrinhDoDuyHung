package com.example.logbook_ex_3.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.logbook_ex_3.Models.Person;

@Dao
public interface PersonDao {
    @Insert
    long insertPerson(Person person);

    @Update
    void updatePerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT * FROM persons ORDER BY name")
    List<Person> getAllPersons();
}
package com.example.art_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlDatabaseHelper extends SQLiteOpenHelper {
    private static final String database_name = "sql_database";

    private static final String table_persons = "persons";
    private static final String table_persons_column_id = "id";
    private static final String table_persons_column_status = "status";
    private static final String table_persons_column_name = "name";
    private static final String table_persons_column_amount = "amount";

    private static final String table_entries = "entries";
    private static final String table_entries_column_id = "id";
    private static final String table_entries_column_status = "status";
    private static final String table_entries_column_person_id = "person";
    private static final String table_entries_column_amount = "amount";
    private static final String table_entries_column_comment = "comment";
    private static final String table_entries_column_date = "date";

    private String sqlCommand;

    SqlDatabaseHelper(@Nullable Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTables(SQLiteDatabase db) {
        sqlCommand = "create table "
                + table_persons + " ("
                + table_persons_column_id + " integer primary key autoincrement, "
                + table_persons_column_status + " integer, "
                + table_persons_column_name + " text, "
                + table_persons_column_amount + " integer)";

        db.execSQL(sqlCommand);

        sqlCommand = "create table "
                + table_entries + " ("
                + table_entries_column_id + " integer primary key autoincrement, "
                + table_entries_column_status + " integer, "
                + table_entries_column_person_id + " integer, "
                + table_entries_column_amount + " integer, "
                + table_entries_column_comment + " text, "
                + table_entries_column_date + " text)";

        db.execSQL(sqlCommand);
    }

    public boolean addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(table_persons_column_status, person.getStatus());
        contentValues.put(table_persons_column_name, person.getName().trim());
        contentValues.put(table_persons_column_amount, person.getAmount());

        long personID = db.insert(table_persons, null, contentValues);

        person.setID((int)personID);

        db.close();

        return personID != -1;
    }

    public boolean updPerson(int personID, Person newPerson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(table_persons_column_status, newPerson.getStatus());
        contentValues.put(table_persons_column_name, newPerson.getName().trim());
        contentValues.put(table_persons_column_amount, newPerson.getAmount());

        String whereClause = table_persons_column_id + " = '" + personID + "'";

        int rowsAffected = db.update(table_entries, contentValues, whereClause, null);

        return rowsAffected != 0;
    }

    public boolean delPerson(int personID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = table_persons_column_id + " = '" + personID + "'";

        int rowsAffected = db.delete(table_persons, whereClause, null);

        db.close();

        delPersonEntries(personID); //delete all entries

        return rowsAffected != 0;
    }

    public boolean addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(table_entries_column_status, entry.getStatus());
        contentValues.put(table_entries_column_person_id, entry.getPersonID());
        contentValues.put(table_entries_column_amount, entry.getAmount());
        contentValues.put(table_entries_column_comment, entry.getComment().trim());
        contentValues.put(table_entries_column_comment, entry.getDate().trim());

        long entryID = db.insert(table_entries, null, contentValues);

        entry.setID((int) entryID);

        db.close();

        return entryID != -1;
    }

    public boolean updEntry(int id, Entry newEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(table_entries_column_status, newEntry.getStatus());
        contentValues.put(table_entries_column_person_id, newEntry.getPersonID());
        contentValues.put(table_entries_column_amount, newEntry.getAmount());
        contentValues.put(table_entries_column_comment, newEntry.getComment().trim());

        String whereClause = table_entries_column_id + " = '" + id + "'";

        int rowsAffected = db.update(table_entries, contentValues, whereClause, null);

        return rowsAffected != 0;
    }

    public boolean delEntry(int entryID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = table_entries_column_id + " = '" + entryID + "'";

        int rowsAffected = db.delete(table_entries, whereClause, null);

        db.close();

        return rowsAffected != 0;
    }

    public void delPersonEntries(int personID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = table_entries_column_person_id + " = '" + personID + "'";

        db.delete(table_entries, whereClause, null);

        db.close();
    }

    public Person getPerson(int id) {
        Person person;

        SQLiteDatabase db = this.getReadableDatabase();

        sqlCommand = "select * from " + table_persons + " where "
                + table_persons_column_id + " = '" + id + "'";

        Cursor cursor = db.rawQuery(sqlCommand, null);

        if (cursor.moveToFirst()) {
            person = new Person(
                    cursor.getInt(cursor.getColumnIndex(table_persons_column_id)),
                    cursor.getInt(cursor.getColumnIndex(table_persons_column_status)),
                    cursor.getString(cursor.getColumnIndex(table_persons_column_name)),
                    cursor.getInt(cursor.getColumnIndex(table_persons_column_amount)));
        }
        else {
            person = null;
        }

        cursor.close();
        db.close();

        return person;
    }

    public ArrayList<Person> getPersonArrayList(int status) {
        ArrayList<Person> personArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        sqlCommand = "select * from " + table_persons + " where "
                + table_persons_column_status + " = '" + status + "'";

        Cursor cursor = db.rawQuery(sqlCommand, null);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person(
                        cursor.getInt(cursor.getColumnIndex(table_persons_column_id)),
                        cursor.getInt(cursor.getColumnIndex(table_persons_column_status)),
                        cursor.getString(cursor.getColumnIndex(table_persons_column_name)),
                        cursor.getInt(cursor.getColumnIndex(table_persons_column_amount)));

                personArrayList.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return personArrayList;
    }

    public ArrayList<Entry> getEntryArrayList(int personID) {
        ArrayList<Entry> entryArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        sqlCommand = "select * from " + table_entries + " where " + table_persons_column_id + " = '" + personID + "'";
        Cursor cursor = db.rawQuery(sqlCommand, null);

        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry(
                        cursor.getInt(cursor.getColumnIndex(table_entries_column_id)),
                        cursor.getInt(cursor.getColumnIndex(table_entries_column_status)),
                        cursor.getInt(cursor.getColumnIndex(table_entries_column_person_id)),
                        cursor.getInt(cursor.getColumnIndex(table_entries_column_amount)),
                        cursor.getString(cursor.getColumnIndex(table_entries_column_comment)),
                        cursor.getString(cursor.getColumnIndex(table_entries_column_date)));

                entryArrayList.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return entryArrayList;
    }

    public static String getPersonsTableName() {
        return table_persons;
    }

    public static String getEntriesTableName() {
        return table_entries;
    }
}

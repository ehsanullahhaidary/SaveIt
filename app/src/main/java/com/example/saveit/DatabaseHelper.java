package com.example.saveit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Initial database name and table
    private static final String DATABASE_NAME = "database_name";
    private static final String TABLE_NAME = "table_name";
    private SQLiteDatabase sqLiteDatabase;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String createTable = "create table " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY, txt TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addText(String text) {
        //Get writable database
        sqLiteDatabase = this.getWritableDatabase();
        //Create ContentValues
        ContentValues contentValues = new ContentValues();
            //add values into database
            contentValues.put("txt", text);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        return true;
    }

    public void removeText() {

        sqLiteDatabase = this.getWritableDatabase();
        //Create ContentValues
        if (sqLiteDatabase == null){
            return;
        }
        sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, null, null);
    }


    public ArrayList getAllText() {
        //Get readable database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        //Create Cursor to select all values
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return arrayList;
    }


}

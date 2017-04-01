package rayckaprojects.tfg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RayckaPC on 02/03/17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Contacts table comands
    private static final String TABLE_COMANDS = "comands";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";
    private static final String KEY_CONTENT = "content";


    public DatabaseHandler(Context context,String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMANDS_TABLE = "CREATE TABLE " + TABLE_COMANDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_NAME + " NAME,"+ KEY_CONTENT + " TEXT"
                + ")";
        db.execSQL(CREATE_COMANDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMANDS);

        // Create tables again
        onCreate(db);
    }
    // Adding new comand
    public void addComand(Comand comand) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,comand.getComand_id());
        values.put(KEY_NAME, comand.getComandName()); // Comand Name
        values.put(KEY_CONTENT, comand.getComandText()); // Comand Text Value

        // Inserting Row
        db.insert(TABLE_COMANDS, null, values);
        db.close(); // Closing database connection

    }

    // Getting single Comand
    public Comand getComand(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMANDS, new String[] { KEY_ID,
                        KEY_NAME, KEY_CONTENT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Comand comand = new Comand(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return comand;
    }



    // Getting All Comands
    public List<Comand> getAllComands() {
        List<Comand> comandList = new ArrayList<Comand>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMANDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Comand comand = new Comand();
                comand.setComand_id(Integer.parseInt(cursor.getString(0)));
                comand.setComandName(cursor.getString(1));
                comand.setComandText(cursor.getString(2));
                // Adding contact to list
                comandList.add(comand);
            } while (cursor.moveToNext());
        }

        // return contact list
        return comandList;
    }

    // Getting Comands Count
    public int getComandsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COMANDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single Comand
    public int updateComand(Comand comand) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comand.getComandName());
        values.put(KEY_CONTENT, comand.getComandText());

        // updating row
        return db.update(TABLE_COMANDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(comand.getComand_id()) });
    }

    // Deleting single Comand, DO NOT USE FROM SAFETY PURPOSES!!!
    public void deleteComand(Comand comand) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMANDS, KEY_ID + " = ?",
                new String[] { String.valueOf(comand.getComand_id()) });
        db.close();
    }


    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        int contador=getComandsCount();
        for (int i=1;i<=contador;i++){
            db.delete(TABLE_COMANDS, KEY_ID + " = ?",
                    new String[] { String.valueOf(i) });

        }
        db.close();
    }
    public void addBasicComands(){
        addComand(new Comand(1,"llama a","call"));
        addComand(new Comand(2,"hola","showcomands"));
        addComand(new Comand(3,"añadir alarma","alarm"));
        addComand(new Comand(4,"mail","mail"));
        addComand(new Comand(5,"añadir comando","addComand"));
        addComand(new Comand(6,"luz","lightmodifier"));
        addComand(new Comand(7,"añadir contacto","addcontact"));

    }

}

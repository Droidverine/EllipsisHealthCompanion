package com.droidverine.ellipsis.ellipsishealthcompanion.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Messages;
import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Uploaddocs;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.data;
import static android.R.attr.directBootAware;
import static android.R.attr.logo;
import static android.R.attr.version;
import static com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager.DOCS_COLUMN_IMG;
import static com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager.DOCS_COLUMN_TITLE;
import static com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager.DOCS_TABLE;

/**
 * Created by DELL on 13-12-2017.
 */

public class Offlinedatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Ellipsisdatabase.db";
    public static final String MESSAGES_TABLE_NAME = "MessagesTable";
    public static final String MESSAGES_COLUMN_ID = "msgid";
    public static final String DOCS_ID="docsid";
    public static final String DOCS_COLUMN_TITLE = "docstitle";
    public static final String DOCS_COLUMN_IMG = "docsimg";

    public static final String SPONSORS_ID="sponsorid";
    public static final String SCHEDULE_ID="scheduleid";

    public static final String MESSAGES_COLUMN_TITLE = "msghead";
    public static final String MESSAGES_COLUMN_BODY = "msgbody";
    private static final String TRUNCATE_MESSAGES = "DELETE FROM " + MESSAGES_TABLE_NAME;
    private static final String TRUNCATE_SPONSORS = "DELETE FROM " + DetailsManager.SPONSOR_TABLE;
    private static final String TRUNCATE_SCHEDULe= "DELETE FROM " + DetailsManager.Schedule_TABLE;
    private static final String TRUNCATE_DOCS = "DELETE FROM " + DetailsManager.DOCS_TABLE;

    private static final String DELETE_NOTIF_TABLE = "DROP TABLE IF EXISTS " + MESSAGES_TABLE_NAME;
    private static final String CREATE_NOTIF_TABLE = "CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLE_NAME + " ( " +
            MESSAGES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MESSAGES_COLUMN_TITLE + " VARCHAR DEFAULT NULL, " +
            MESSAGES_COLUMN_BODY +" , "+DetailsManager.MESSAGES_COLUMN_TIME +" VARCHAR DEFAULT NULL );";

    private static final String DELETE_SPONS_TABLE = "DROP TABLE IF EXISTS " + DetailsManager.SPONSOR_TABLE;
    private static final String CREATE_SPONS_TABLE = "CREATE TABLE IF NOT EXISTS " + DetailsManager.SPONSOR_TABLE + " ( " +
            SPONSORS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DetailsManager.SPONSOR_TITLE + " VARCHAR DEFAULT NULL, " +
            DetailsManager.SPONSOR_DETAILS +" VARCHAR DEFAULT NULL, "+ DetailsManager.SPONSOR_IMG + " VARCHAR DEFAULT NULL" + " , "+DetailsManager.SPONSOR_LINK +" VARCHAR DEFAULT NULL );";

    private static final String DELETE_SCHEDULE_TABLE = "DROP TABLE IF EXISTS " + DetailsManager.SPONSOR_TABLE;

    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS " + DetailsManager.Schedule_TABLE + " ( " +
            SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DetailsManager.Schedule_COLUMN_TITLE + " VARCHAR DEFAULT NULL, " +
            DetailsManager.Schedule_COLUMN_TIME +" VARCHAR DEFAULT NULL );";

    private static final String DELETE_DOCS_TABLE = "DROP TABLE IF EXISTS " + DetailsManager.DOCS_TABLE;
    private static final String CREATE_DOCS_TABLE = "CREATE TABLE IF NOT EXISTS " + DetailsManager.DOCS_TABLE + " ( " +
            DOCS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DetailsManager.DOCS_TITLE + " VARCHAR DEFAULT NULL, " +
            DetailsManager.DOCS_IMG +" VARCHAR DEFAULT NULL );";



    public Offlinedatabase(Context context) {
        super(context,DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTIF_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_SCHEDULE_TABLE);
        sqLiteDatabase.execSQL(CREATE_DOCS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




    public void insertNotifications(HashMap<String, String> map) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESSAGES_COLUMN_TITLE, map.get(DetailsManager.MESSAGES_COLUMN_TITLE));
        values.put(MESSAGES_COLUMN_BODY, map.get(DetailsManager.MESSAGES_COLUMN_BODY));
        values.put(DetailsManager.MESSAGES_COLUMN_TIME,map.get(DetailsManager.MESSAGES_COLUMN_TIME));

        db.insert(MESSAGES_TABLE_NAME, null, values);
        Log.d("inserted","data");

    }
    public void truncateNotifications()
    {
        SQLiteDatabase database=getWritableDatabase();
        Log.d("table truncated","dropped");
        database.execSQL(TRUNCATE_MESSAGES);
    }
    public ArrayList<Messages> getAllNotifications()
    {
        ArrayList<Messages> messagesList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(MESSAGES_TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        //Cursorr cursoror=db.rawQuery("SELECT * FROM "+Constants.NOTIFICATIONS_TABLE_NAME+" ORDER BY "+Constants.NOTIFICATION_TIMESTAMP,null);
        //for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            Messages notification_data = new Messages();
            notification_data.setMsghead(cursor.getString(cursor.getColumnIndex(MESSAGES_COLUMN_TITLE)));
            notification_data.setMsgtext(cursor.getString(cursor.getColumnIndex(MESSAGES_COLUMN_BODY)));
            Long l=Long.parseLong(cursor.getString(cursor.getColumnIndex(DetailsManager.MESSAGES_COLUMN_TIME)));
            notification_data.setMsgtime(l);
            messagesList.add(notification_data);
        }
        cursor.close();
        db.close();
        return messagesList;
    }

    //DOCS STARTS HERE

    public void insertDocs(HashMap<String, String> map) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCS_COLUMN_TITLE, map.get(DetailsManager.DOCS_COLUMN_TITLE));
        values.put(DOCS_COLUMN_IMG, map.get(DetailsManager.DOCS_COLUMN_IMG));
        db.insert(DOCS_TABLE, null, values);
        Log.d("DOCSDATA","inserted");

    }

    public void truncateDocs()
    {
        SQLiteDatabase database=getWritableDatabase();
        Log.d("table truncated","dropped");
        database.execSQL(TRUNCATE_DOCS);
    }
    public ArrayList<Uploaddocs> getAllDocs()
    {
        ArrayList<Uploaddocs> uploaddocslist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DOCS_TABLE, new String[]{"*"}, null, null, null, null, null);
        //Cursorr cursoror=db.rawQuery("SELECT * FROM "+Constants.NOTIFICATIONS_TABLE_NAME+" ORDER BY "+Constants.NOTIFICATION_TIMESTAMP,null);
        //for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            Log.d("docs",""+cursor.getString(cursor.getColumnIndex(DOCS_COLUMN_TITLE)));
            Uploaddocs notification_data = new Uploaddocs();
            notification_data.setName(cursor.getString(cursor.getColumnIndex(DOCS_COLUMN_TITLE)));
            notification_data.setImgurl(cursor.getString(cursor.getColumnIndex(DOCS_COLUMN_IMG)));
            uploaddocslist.add(notification_data);
        }
        cursor.close();
        db.close();
        return uploaddocslist;
    }
}

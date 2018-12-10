package samueljdecanio.com.mynotify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import samueljdecanio.com.mynotify.database.NotificationDatabaseSchema.NotificationTable;


public class NotificationDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 4;
    private static final String DATABASE_NAME = "notificationBase.db";

    public NotificationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NotificationTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NotificationTable.Cols.UUID + ", " +
                NotificationTable.Cols.TITLE + ", " +
                NotificationTable.Cols.DATE + "," +
                NotificationTable.Cols.DETAILS + ","  +
                NotificationTable.Cols.LINKS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("NotificationDbHelper", "onUpgrade() for DB Schema was called.");
        db.execSQL("DROP TABLE IF EXISTS "+NotificationTable.NAME);
        onCreate(db);
    }

}

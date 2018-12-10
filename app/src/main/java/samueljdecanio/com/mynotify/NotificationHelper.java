package samueljdecanio.com.mynotify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import samueljdecanio.com.mynotify.database.NotificationCursorWrapper;
import samueljdecanio.com.mynotify.database.NotificationDatabaseHelper;
import samueljdecanio.com.mynotify.database.NotificationDatabaseSchema.NotificationTable;

public class NotificationHelper {
    private static NotificationHelper sNotificationHelper;
    private List<OurNotification> mNotifications;
    private SQLiteDatabase mDatabase;

    public static NotificationHelper get(Context context) {
        if(sNotificationHelper == null) {
            sNotificationHelper = new NotificationHelper(context);
        }

        return sNotificationHelper;
    }

    private NotificationHelper(Context context) {
        mNotifications = new ArrayList< >();
        mDatabase = new NotificationDatabaseHelper(context).getWritableDatabase();

    }

    public void addNotification(OurNotification n) {
        ContentValues values = getContentValues(n);
        mDatabase.insert(NotificationTable.NAME, null, values);
    }

    public void deleteNotification(OurNotification n) {
        mDatabase.delete(NotificationTable.NAME,
                NotificationTable.Cols.UUID + " = ?",
                new String[] { n.getId().toString() }
        );
    }

    public List<OurNotification> getOurNotifications() {
        List<OurNotification> notifications = new ArrayList<>();

        NotificationCursorWrapper cursor = queryNotifications(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notifications.add(cursor.getNotification());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notifications;
    }

    public void updateNotification(OurNotification notification) {
        String uuidString = notification.getId().toString();
        ContentValues values = getContentValues(notification);

        mDatabase.update(NotificationTable.NAME, values,
                NotificationTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }


    public OurNotification getOurNotification(UUID id) {
        NotificationCursorWrapper cursor = queryNotifications(
                NotificationTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNotification();
        } finally {
            cursor.close();
        }
    }

    private NotificationCursorWrapper queryNotifications(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NotificationTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new NotificationCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(OurNotification notification) {
        ContentValues values = new ContentValues();
        values.put(NotificationTable.Cols.UUID, notification.getId().toString());
        values.put(NotificationTable.Cols.TITLE, notification.getTitle());
        values.put(NotificationTable.Cols.DETAILS, notification.getDetails());
        values.put(NotificationTable.Cols.DATE, notification.getDateTimeOfNotification().getTimeInMillis());
        values.put(NotificationTable.Cols.LINKS, SerializeLinks(notification.getLinks()));

        Log.i("NotificationHelper", "Serialized Links:" + SerializeLinks(notification.getLinks()));

        return values;
    }

    private static String SerializeLinks(ArrayList<String> listLinks) {
        if(listLinks != null) {
            String stringList = android.text.TextUtils.join(" ", listLinks);
            return stringList;
        }

        return "";
    }
}

package samueljdecanio.com.mynotify.database;

public class NotificationDatabaseSchema {
    public static final class NotificationTable {
        public static final String NAME = "notifications";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DETAILS = "details";
            public static final String LINKS = "links";
        }
    }
}

package live.tracking.bus.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import live.tracking.bus.logging.L;

/**
 * Created by Windows on 31-01-2015.
 */
public class BusOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "businfodatabase";
    public static final String TABLE_NAME = "businfo";
    public static final int DATABASE_VERSION = 1;
    public static final String UID = "_id";
    public static final String BUS_NO = "busNumber";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";
    //private static final String NO="no";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BUS_NO + " VARCHAR(255),"
            + SOURCE + " VARCHAR(255),"
            + DESTINATION + " VARCHAR(255))";
    private static final String DROP_TABLE = "DROP TABLE  IF EXISTS " + TABLE_NAME;
    private Context context;

    public BusOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        L.t(context, "constructor called");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(CREATE_TABLE);
            L.t(context, "oncreate called");
        } catch (SQLiteException e) {
            L.t(context, "" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            L.t(context, "onupgrade called");
        } catch (SQLiteException e) {
            L.t(context, "" + e);
        }
    }
}
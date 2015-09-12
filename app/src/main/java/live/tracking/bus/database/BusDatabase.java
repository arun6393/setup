package live.tracking.bus.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BusDatabase extends ContentProvider {
    private Context context;
    private static final String AUTHORITY = "live.tracking.bus.database";
    private static final int BUSES = 10;
    private static final int BUS_ID = 20;
    private static final String BASE_PATH = "buses";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final UriMatcher uriMatcher;
    private BusOpenHelper busOpenHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "buses", BUSES);
        uriMatcher.addURI(AUTHORITY, "buses/#", BUS_ID);
    }

    private SQLiteDatabase db;

    public BusDatabase() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            // Get all friend-birthday records
            case BUSES:
                return "vnd.android.cursor.dir/vnd.example.friends";
            // Get a particular friend
            case BUS_ID:
                return "vnd.android.cursor.item/vnd.example.friends";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long row = db.insert(BusOpenHelper.TABLE_NAME, "", values);

        // If record is added successfully
        if(row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;

    }

    @Override
    public boolean onCreate() {
        context = getContext();
        busOpenHelper = new BusOpenHelper(context);
        db = busOpenHelper.getWritableDatabase();
        // TODO: Implement this to initialize your content provider on startup.
        if (db == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

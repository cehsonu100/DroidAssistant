package mydroid.com.droidassistant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sonu & Roshni on 4/9/2016.
 */
public class DBhelper extends SQLiteOpenHelper{

    //Table name
    public static final String TABLE_NAME="TwitchDB";

    //column name
    public static final String _ID = "_id";
    public static final String PLACE_NAME = "place_name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RADIUS = "radius";
    public static final String MODE = "mode";
    //DB Info
    static final String DB_NAME = "TwitchDBase.db";
    static final int DB_VERSION = 5;


    public DBhelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_NAME + " ("+_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+PLACE_NAME+ " TEXT, "+LATITUDE+ " REAL, "+LONGITUDE+ " REAL, "+RADIUS+ " REAL, "+MODE+ " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table if exists "+TABLE_NAME);
        onCreate(db);
    }
}

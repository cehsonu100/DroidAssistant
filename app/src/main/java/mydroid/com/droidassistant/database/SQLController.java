package mydroid.com.droidassistant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by Sonu & Roshni on 4/9/2016.
 */
public class SQLController {
    private DBhelper dBHelper;
    private Context ourContex;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        this.ourContex=c;
    }

    public SQLController open()  throws SQLException{
        dBHelper = new DBhelper(ourContex);
        database = dBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dBHelper.close();
    }

    public void insert (String place_name, double latitude, double longitude, float radius, String mode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBhelper.PLACE_NAME,place_name);
        contentValues.put(DBhelper.LATITUDE,latitude);
        contentValues.put(DBhelper.LONGITUDE,longitude);
        contentValues.put(DBhelper.RADIUS,radius);
        contentValues.put(DBhelper.MODE,mode);
        database.insert(DBhelper.TABLE_NAME, null, contentValues);
        Log.i("Insert staus ", "inserted");
    }

    public Cursor fetch() {
        String[] columns=new String[] {DBhelper._ID,DBhelper.PLACE_NAME,DBhelper.LATITUDE,DBhelper.LONGITUDE,DBhelper.RADIUS,DBhelper.MODE};
        Cursor cursor = database.query(DBhelper.TABLE_NAME,columns,null,null,null,null,null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(int id) {
        database.delete(DBhelper.TABLE_NAME,DBhelper._ID+"="+id,null);
    }

    public void deleteAll() {
        database.delete(DBhelper.TABLE_NAME,null,null);
    }
}

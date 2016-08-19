package mydroid.com.droidassistant.TODO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;

/**
 * Created by Roshni on 6/9/2016.
 */
public class GetContactNumber {
    String mobile_number = "Not saved";

    public String getNumber(String name, Context stt_context) {
        name=name.trim();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = stt_context.getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name2   = people.getString(indexName);
            name2=name2.toLowerCase();
            System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii  "+name2);
            if(name2.equals(name)) {
                mobile_number = people.getString(indexNumber);
                System.out.println("byyyyyyyyyyyyyyyyyyyyyyyyeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee " +mobile_number);
                break;
            }
        } while (people.moveToNext());

        return mobile_number;
    }
}
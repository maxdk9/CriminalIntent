package criminalintent.and.mazzy.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import criminalintent.and.mazzy.criminalintent.db.CrimeBaseHelper;
import criminalintent.and.mazzy.criminalintent.db.CrimeCursorWrapper;
import criminalintent.and.mazzy.criminalintent.db.CrimeDbSchema;

public class CrimeLab {
    private static  CrimeLab ourInstance ;

    private List <UUID> changed=new ArrayList<UUID>();
    private Context mContext;
    private SQLiteDatabase mDatabase;



    public static CrimeLab getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new CrimeLab(context);

        }
        return ourInstance;
    }


    private static ContentValues getContentValues(Crime crime) {
        ContentValues res = new ContentValues();
        res.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getUid().toString());
        res.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved());
        res.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        res.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        res.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());
        res.put(CrimeDbSchema.CrimeTable.Cols.SUSPECTID,crime.getSuspectId());
        return res;

    }

    private CrimeLab(Context context) {


        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();


    }

    private void GeterateTestCrimes() {
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Для каждого второго объекта
            crime.setRequiredPolice(i%10==0);
        }
    }


    public Crime GetCrime(UUID uuid) {

        CrimeCursorWrapper cursorWrapper=queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",
                new String[]{uuid.toString()}
        );
        try{
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            else {
                cursorWrapper.moveToFirst();
                return cursorWrapper.getCrime();
            }
        }
        finally {
            cursorWrapper.close();
        }


    }

    public List<Crime> getCrimes() {

        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return crimes;
    }

    public void AddCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeDbSchema.NAME, null, values);
    }


    public void AddChanged(UUID uid) {
        if (changed.contains(uid)) {
            return;
        }
        changed.add(uid);
    }


    public void UpdateCrime(Crime crime) {
        String uuidString=crime.getUid().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.NAME, values, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }



    public List<UUID> getChanged() {
        return changed;
    }

    public void DeleteCrime(Crime crime) {

        String uuidString=crime.getUid().toString();
        mDatabase.delete(CrimeDbSchema.NAME,CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",new String[]{uuidString});
    }


    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeDbSchema.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    public File GetPhotoFile(Crime mCrime) {
        File fileDir=mContext.getFilesDir();
        return new File(fileDir, mCrime.getFileName());
    }
}

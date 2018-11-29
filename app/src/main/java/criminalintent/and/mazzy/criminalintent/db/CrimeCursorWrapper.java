package criminalintent.and.mazzy.criminalintent.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import criminalintent.and.mazzy.criminalintent.Crime;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));
        String suspectid = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECTID));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setSolved(isSolved > 0);
        crime.setTitle(title);
        crime.setSuspect(suspect);

        return crime;


    }
}

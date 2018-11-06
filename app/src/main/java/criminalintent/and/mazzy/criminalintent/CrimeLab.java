package criminalintent.and.mazzy.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class CrimeLab {
    private static  CrimeLab ourInstance ;
    private List<Crime> mCrimes;
    private List <UUID> changed=new ArrayList<UUID>();


    static CrimeLab getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new CrimeLab(context);

        }
        return ourInstance;
    }

    private CrimeLab(Context context) {

        mCrimes = new ArrayList<>();
        GeterateTestCrimes();

    }

    private void GeterateTestCrimes() {
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Для каждого второго объекта
            crime.setRequiredPolice(i%10==0);
            mCrimes.add(crime);
        }
    }


    public Crime GetCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getUid().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }


    public void AddChanged(UUID uid) {
        if (changed.contains(uid)) {
            return;
        }
        changed.add(uid);
    }

    public List<UUID> getChanged() {
        return changed;
    }
}

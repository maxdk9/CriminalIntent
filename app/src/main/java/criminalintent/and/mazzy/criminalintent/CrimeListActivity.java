package criminalintent.and.mazzy.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CrimeListFragment();
        return fragment;
    }
}

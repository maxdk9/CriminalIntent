package criminalintent.and.mazzy.criminalintent;

import android.support.v4.app.Fragment;

import criminalintent.and.mazzy.criminalintent.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks , CrimeFragment.Callbacks {

    @Override
    public void onCrimeSelected(Crime crime) {
        super.onCrimeSelected(crime);
    }

    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CrimeListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment=(CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        crimeListFragment.UpdateUI();
    }
}

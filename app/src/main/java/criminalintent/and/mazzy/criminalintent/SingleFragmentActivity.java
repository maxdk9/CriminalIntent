package criminalintent.and.mazzy.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import criminalintent.and.mazzy.criminalintent.model.Crime;
import criminalintent.and.mazzy.criminalintent.page.CrimePageActivity;

public abstract  class SingleFragmentActivity extends AppCompatActivity implements CrimeListFragment.Callbacks {
    protected abstract Fragment createFragment();

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=CrimePageActivity.newIntent(this,crime.getid);
            startActivity(intent);
        }

        else{
            Fragment newDetail=CrimeFragment.getInstance(crime.getUid());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,newDetail).commit();

        }
    }


    @LayoutRes
    protected int getLayoutResId() {

        return R.layout.activity_masterdetail;
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}

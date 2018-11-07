package criminalintent.and.mazzy.criminalintent.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import criminalintent.and.mazzy.criminalintent.Crime;

import criminalintent.and.mazzy.criminalintent.CrimeFragment;
import criminalintent.and.mazzy.criminalintent.CrimeLab;
import criminalintent.and.mazzy.criminalintent.R;

public class CrimePageActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String EXTRA_CRIME_ID="criminalintent.and.mazzy.criminalintent.page";
    ViewPager mViewPager;
    List<Crime> mCrimes;
    Button btnFirstCrime;
    Button btnLastCrime;



    public static Intent newIntent(Context context, UUID uuid) {
        Intent res=new Intent(context,CrimePageActivity.class);
        res.putExtra(CrimePageActivity.EXTRA_CRIME_ID, uuid);
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        btnFirstCrime = findViewById(R.id.buttonFirst);
        btnLastCrime = findViewById(R.id.buttonLast);
        btnFirstCrime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                mViewPager.setCurrentItem(0);
                UpdateUI();
                return true;
            }
        });

        btnLastCrime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);
                UpdateUI();
                return true;
            }
        });

        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = findViewById(R.id.crime_view_pager);

          FragmentManager fragmentManager = getSupportFragmentManager();
        mCrimes = CrimeLab.getInstance(this).getCrimes();


        int currentItemIndex=0;
        for (Crime crime : mCrimes) {
            if (crime.getUid().equals(uuid)) {
                currentItemIndex = mCrimes.indexOf(crime);
                break;
            }
        }


        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.getInstance(crime.getUid());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setCurrentItem(currentItemIndex);
        UpdateUI();

    }

    @Override
    public void onClick(View view) {

        Log.d("onClick","Start onclick listener");
        switch (view.getId()){
            case R.id.buttonFirst:
                mViewPager.setCurrentItem(0);
                UpdateUI();
                break;

            case R.id.buttonLast:
                mViewPager.setCurrentItem(mCrimes.size()-1);
                UpdateUI();
                break;

                default:
                    break;

        }

    }

    private void UpdateUI() {
        btnLastCrime.setEnabled(true);
        btnFirstCrime.setEnabled(true);
        if (mViewPager.getCurrentItem() == mCrimes.size() - 1) {
            btnLastCrime.setEnabled(false);
        }
        if (mViewPager.getCurrentItem() == 0) {
            btnFirstCrime.setEnabled(false);
        }
    }
}

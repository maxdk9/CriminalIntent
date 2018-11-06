package criminalintent.and.mazzy.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "extra_crime_id";





    public static Intent newIntent(Context context, UUID crimeuuid) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeuuid);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeuuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.getInstance(crimeuuid);
    }



}

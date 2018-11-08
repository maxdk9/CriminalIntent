package criminalintent.and.mazzy.criminalintent;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import criminalintent.and.mazzy.criminalintent.page.CrimePageActivity;

public class CrimeFragment extends Fragment {

    public static final String CRIME_ID = "crime_id";
    public static final String DIALOG_DATE = "dialogdate";
    public static final int REQUEST_DATE=0;

    Crime mCrime;
    private EditText mEditText;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode!= Activity.RESULT_OK)
       {
           return;
       }

        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mCrime.setDate(date);

        update_mDate(date);


    }

    private void update_mDate(Date date) {
        mDateButton.setText(date.toString());
    }

    public static CrimeFragment getInstance(UUID crimeuuid) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeuuid);
        CrimeFragment result = new CrimeFragment();
        result.setArguments(args);
        return result;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).GetCrime(crimeId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime, container,false);
        mEditText = v.findViewById(R.id.crimeTitle);
        mEditText.setText(mCrime.getTitle());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                CrimeLab.getInstance(getActivity()).AddChanged(mCrime.getUid());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = v.findViewById(R.id.crimeDate);
        update_mDate(mCrime.getDate());
        mDateButton.setEnabled(true);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fragmentManager,DIALOG_DATE);

            }
        });




        mSolvedCheckbox = v.findViewById(R.id.crimeSolved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        return v;
    }
}

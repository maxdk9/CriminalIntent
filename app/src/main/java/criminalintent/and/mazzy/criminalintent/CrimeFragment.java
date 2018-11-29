package criminalintent.and.mazzy.criminalintent;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import criminalintent.and.mazzy.criminalintent.page.CrimePageActivity;

import static android.provider.LiveFolders.INTENT;

public class CrimeFragment extends Fragment {

    public static final String CRIME_ID = "crime_id";
    public static final String DIALOG_DATE = "dialogdate";
    public static final int REQUEST_DATE=0;
    private static final int REQUEST_CONTACT =1 ;

    Crime mCrime;
    private EditText mEditText;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDeleteButton;

    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallSuspectButton;




    private CheckBox mSolvedCheckbox;

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).UpdateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {

            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mCrime.setDate(date);

            update_mDate(date);
            update_Time(date);
        }

        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID
            };

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {

                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                String suspectid = c.getString(1);
                mCrime.setSuspect(suspect);
                mCrime.setSuspectId(suspectid);
                mSuspectButton.setText(suspect+";id="+suspectid);
            } finally {
                c.close();
            }
        }

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
    public void onResume() {
        super.onResume();


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


        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mReportButton = v.findViewById(R.id.crime_report);

        mDateButton = v.findViewById(R.id.crimeDate);
        mCallSuspectButton = v.findViewById(R.id.call_suspect);

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


        mTimeButton = v.findViewById(R.id.crimeTime);
        update_Time(mCrime.getDate());
        mTimeButton.setEnabled(true);

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
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

        mDeleteButton = v.findViewById(R.id.crimeDelete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimeLab.getInstance(getActivity()).DeleteCrime(mCrime);
                getActivity().finish();
            }
        });

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });


        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);


        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnClickListener","OnClickListener +mSuspectButton");
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });



        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }




        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = getPhoneUsingContactId();
                Toast.makeText(getActivity(),"Phone:"+phone,Toast.LENGTH_LONG).show();
            }
        });
        if (mCrime.getSuspect() == null) {
            mCallSuspectButton.setEnabled(false);
        }

        return v;
    }

    public String getPhoneUsingContactId(){

        String contactId=mCrime.getSuspectId();
        String cContactIdString = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Uri cCONTACT_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String cDisplayNameColumn = ContactsContract.CommonDataKinds.Phone.NUMBER;

        String selection = cContactIdString + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(contactId)};

        Cursor cursor = getActivity().getContentResolver().query(cCONTACT_CONTENT_URI, null, selection, selectionArgs, null);


        String phone = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            while ((cursor != null) && (cursor.isAfterLast() == false)) {
                if (cursor.getColumnIndex(cContactIdString) >= 0) {
                    if (contactId.equals(cursor.getString(cursor.getColumnIndex(cContactIdString)))) {
                       phone = cursor.getString(cursor.getColumnIndex(cDisplayNameColumn));
                        break;
                    }
                }
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        return phone;
    }

    private void update_Time(Date date) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm", Locale.ENGLISH);
        String restime = dateFormat.format(date);
        mTimeButton.setText(restime);
    }


    private String getCrimeReport() {
        String solvedString=null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report);

        }
        else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        SimpleDateFormat dFormat = new SimpleDateFormat(dateFormat,Locale.ENGLISH);


        String dateString =dFormat.format(mCrime.getDate()).toString();

        String suspect=mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);



        return solvedString;
    }
}

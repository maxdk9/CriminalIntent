package criminalintent.and.mazzy.criminalintent;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;
    public final static String EXTRA_DATE="criminalintent.and.mazzy.criminalintent";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date crimeDate=(Date)getArguments().getSerializable(CrimeFragment.DIALOG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(crimeDate);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker= v.findViewById(R.id.dialog_data_picker);
        mDatePicker.init(year, month, day,null);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date resultDate = new GregorianCalendar(year, month, day).getTime();
                        SendResult(Activity.RESULT_OK,resultDate);
                    }
                }).create();

    }


    public static  DatePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CrimeFragment.DIALOG_DATE, date);

        DatePickerFragment result = new DatePickerFragment();
        result.setArguments(bundle);
        return result;
    }

    public void SendResult(int resultCode, Date date) {
        if (this.getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);

    }
}

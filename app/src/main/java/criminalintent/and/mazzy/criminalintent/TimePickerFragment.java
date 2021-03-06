package criminalintent.and.mazzy.criminalintent;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    private TimePicker mTimePicker;
    public final static String EXTRA_TIME = "criminalintent.and.mazzy.criminalintent.extratime";
    Date editedDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editedDate = (Date) getArguments().getSerializable(CrimeFragment.DIALOG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(editedDate);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);
        return new AlertDialog.Builder(getActivity()).setView(view).setTitle("Time of crime").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hour = mTimePicker.getHour();
                int minute = mTimePicker.getMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(editedDate);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                SendResult(Activity.RESULT_OK, calendar.getTime());
            }
        }).create();

    }


    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CrimeFragment.DIALOG_DATE, date);

        TimePickerFragment result = new TimePickerFragment();
        result.setArguments(bundle);
        return result;
    }


    public void SendResult(int resultCode, Date date) {
        if (this.getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DatePickerFragment.EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
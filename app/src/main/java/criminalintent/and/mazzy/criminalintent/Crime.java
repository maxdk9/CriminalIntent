package criminalintent.and.mazzy.criminalintent;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mUid;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiredPolice;
    private String mSuspect;
    private String mSuspectId;



    private static String dateFormatString="EEEE, MMM d, yyyy";//Friday, Jul 22, 2016

    public Crime(UUID uuid) {
        this.mUid=uuid;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }



    public Crime() {
        this.mDate = new Date();
        this.mUid=UUID.randomUUID();
    }

    public UUID getUid() {
        return mUid;
    }

    public boolean isRequiredPolice() {
        return mRequiredPolice;
    }

    public void setRequiredPolice(boolean requiredPolice) {
        this.mRequiredPolice = requiredPolice;
    }

    public String getDateString() {
        String res=android.text.format.DateFormat.format(dateFormatString, this.getDate()).toString();


        return res;
    }


    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public void setSuspectId(String id) {
        this.mSuspectId=id;
    }

    public String getSuspectId() {
        return this.mSuspectId;
    }

    public String getFileName() {
        return "IMG_" + this.getUid() + ".jpg";
    }

}

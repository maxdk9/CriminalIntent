package criminalintent.and.mazzy.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mUid;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }



    public Crime() {

        this.mDate = new Date();
        this.mUid=UUID.randomUUID();
    }
}

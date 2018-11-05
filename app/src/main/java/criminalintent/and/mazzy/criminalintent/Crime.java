package criminalintent.and.mazzy.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mUid;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiredPolice;

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
}

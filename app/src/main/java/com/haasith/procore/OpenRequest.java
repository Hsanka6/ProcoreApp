package com.haasith.procore;

import android.os.Parcel;
import android.os.Parcelable;

public class OpenRequest implements Parcelable {
    int prNumber;
    String prTitle;

    public OpenRequest(int prNumber, String prTitle) {
        this.prNumber = prNumber;
        this.prTitle = prTitle;
    }

    protected OpenRequest(Parcel in) {
        prNumber = in.readInt();
        prTitle = in.readString();
    }


    public static final Creator<OpenRequest> CREATOR = new Creator<OpenRequest>() {
        @Override
        public OpenRequest createFromParcel(Parcel in) {
            return new OpenRequest(in);
        }

        @Override
        public OpenRequest[] newArray(int size) {
            return new OpenRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(prNumber);
        parcel.writeString(prTitle);
    }
}

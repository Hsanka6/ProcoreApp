package com.haasith.procore;

import android.os.Parcel;
import android.os.Parcelable;

public class PullRequest implements Parcelable {
    String oldFile;
    String newFile;
    String lineNum;
    String codeLines;

    public PullRequest(String oldFile, String newFile, String lineNum, String codeLines) {
        this.oldFile = oldFile;
        this.newFile = newFile;
        this.lineNum = lineNum;
        this.codeLines = codeLines;
    }

    public PullRequest() {

    }




    protected PullRequest(Parcel in) {
        oldFile = in.readString();
        newFile = in.readString();
        lineNum = in.readString();
        codeLines = in.readString();
    }

    public static final Creator<PullRequest> CREATOR = new Creator<PullRequest>() {
        @Override
        public PullRequest createFromParcel(Parcel in) {
            return new PullRequest(in);
        }

        @Override
        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };

    public void setOldFile(String oldFile) {
        this.oldFile = oldFile;
    }

    public void setNewFile(String newFile) {
        this.newFile = newFile;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public void setCodeLines(String codeLines) {
        this.codeLines = codeLines;
    }


    public String getOldFile() {
        return oldFile;
    }

    public String getNewFile() {
        return newFile;
    }

    public String getLineNum() {
        return lineNum;
    }

    public String getCodeLines() {
        return codeLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(oldFile);
        parcel.writeString(newFile);
        parcel.writeString(lineNum);
        parcel.writeString(codeLines);

    }
}

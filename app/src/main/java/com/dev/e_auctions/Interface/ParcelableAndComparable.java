package com.dev.e_auctions.Interface;

import android.os.Parcel;
import android.os.Parcelable;

import com.dev.e_auctions.Model.Message;

public interface ParcelableAndComparable extends Parcelable, Comparable<Message> {
    @Override
    int compareTo(Message o);

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel dest, int flags);
}

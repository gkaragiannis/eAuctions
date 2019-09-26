package com.dev.e_auctions.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class User implements Parcelable {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String address;
    private Double bidderRating;
    private Integer bidderRatingVotes;
    private Double sellerRating;
    private Integer sellerRatingVotes;

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public Double getBidderRating() {
        return bidderRating;
    }

    /**
     *
     * @return
     */
    public Integer getBidderRatingVotes() {
        return bidderRatingVotes;
    }

    /**
     *
     * @return
     */
    public Double getSellerRating() {
        return sellerRating;
    }

    /**
     *
     * @return
     */
    public Integer getSellerRatingVotes() {
        return sellerRatingVotes;
    }

    /**
     *
     * @param in
     */
    protected User(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        country = in.readString();
        city = in.readString();
        address = in.readString();
        bidderRating = in.readByte() == 0x00 ? null : in.readDouble();
        bidderRatingVotes = in.readByte() == 0x00 ? null : in.readInt();
        sellerRating = in.readByte() == 0x00 ? null : in.readDouble();
        sellerRatingVotes = in.readByte() == 0x00 ? null : in.readInt();
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(address);
        if (bidderRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(bidderRating);
        }
        if (bidderRatingVotes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(bidderRatingVotes);
        }
        if (sellerRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(sellerRating);
        }
        if (sellerRatingVotes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(sellerRatingVotes);
        }
    }

    /**
     *
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

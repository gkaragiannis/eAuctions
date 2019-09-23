package com.dev.e_auctions.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Message implements Parcelable {
    private int messageId;
    private String messageTime;
    private User sender;
    private User receiver;
    private String subject;
    private String message;
    private Long auctionId;
    private boolean read;

    /**
     *
     * @return
     */
    public Long getAuctionId() {
        return auctionId;
    }

    /**
     *
     * @return
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     *
     * @return
     */
    public String getMessageTime() {
        return messageTime;
    }

    public User getSender() {
        return sender;
    }

    /**
     *
     * @return
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return
     */
    public boolean isRead() {
        return read;
    }

    /**
     *
     */
    public void setRead() {
        this.read = true;
    }

    /**
     *
     * @param in
     */
    protected Message(Parcel in) {
        messageId = in.readInt();
        messageTime = in.readString();
        sender = (User) in.readValue(User.class.getClassLoader());
        receiver = (User) in.readValue(User.class.getClassLoader());
        subject = in.readString();
        message = in.readString();
        auctionId = in.readByte() == 0x00 ? null : in.readLong();
        read = in.readByte() != 0x00;
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
        dest.writeInt(messageId);
        dest.writeString(messageTime);
        dest.writeValue(sender);
        dest.writeValue(receiver);
        dest.writeString(subject);
        dest.writeString(message);
        if (auctionId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(auctionId);
        }
        dest.writeByte((byte) (read ? 0x01 : 0x00));
    }

    /**
     *
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };


}
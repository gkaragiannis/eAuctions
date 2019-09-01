package com.dev.e_auctions.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

    private Utils() {
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}

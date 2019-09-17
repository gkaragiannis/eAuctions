package com.dev.e_auctions.Utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

    private Utils(){}

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

}

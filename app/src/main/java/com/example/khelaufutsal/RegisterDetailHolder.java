package com.example.khelaufutsal;

import java.util.ArrayList;
import java.util.LinkedList;

public class RegisterDetailHolder {
    private static LinkedList allDetails = new LinkedList();

    public static LinkedList getAllDetails() {
        return allDetails;
    }

    public static void setAllDetails(LinkedList allDetails) {
        RegisterDetailHolder.allDetails = allDetails;
    }
    public static void keepDetails(String detail){
        allDetails.add(detail);
    }
}

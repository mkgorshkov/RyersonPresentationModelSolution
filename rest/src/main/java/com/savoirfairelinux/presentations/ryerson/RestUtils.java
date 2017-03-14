package com.savoirfairelinux.presentations.ryerson;

/**
 * Created by mgorshkov on 3/13/17.
 */
public class RestUtils {

    public static final String OUTPUT_FILE = "/home/ubuntu/ryerson_insurance_demo.csv";

    public static String csvFiyHeaders(){
        return "\"firstName\", \"lastName\", \"gender\", \"postalCode\", \"dateOfBirth\", \"vehicleType\", \"make\", \"model\", \"year\", \"condition\""+System.lineSeparator();
    }
    public static String csvFiy(String s){
        return "\""+s+"\",";
    }
    public static String csvFiyEnding(String s){
        return "\""+s+"\""+System.lineSeparator();
    }
}

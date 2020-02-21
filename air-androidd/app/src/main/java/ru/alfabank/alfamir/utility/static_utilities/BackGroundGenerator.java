package ru.alfabank.alfamir.utility.static_utilities;

import java.util.Random;

/**
 * Created by U_M0WY5 on 23.01.2018.
 */

public class BackGroundGenerator {

    public static String getBackgroundAvatarColor(String initials){
        int a = initials.hashCode() % colorLib.length;
        return colorLib[a];
    }

    public static String getBackgroundAvatarColor(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        int a = randomStringBuilder.toString().hashCode() % colorLib.length;
        return colorLib[a];
    }


    private static String[] colorLib = {
            "D32F2F",
            "C62828",
            "B71C1C",
            "C2185B",
            "AD1457",
            "880E4F",
            "7B1FA2",
            "6A1B9A",
            "4A148C",
            "512DA8",
            "4527A0",
            "311B92",
            "303F9F",
            "283593",
            "1A237E",
            "1976D2",
            "1565C0",
            "0D47A1",
            "0288D1",
            "0277BD",
            "01579B",
            "0097A7",
            "00838F",
            "006064",
            "00796B",
            "00695C",
            "004D40",
            "388E3C",
            "2E7D32",
            "1B5E20",
            "689F38",
            "558B2F",
            "33691E",
            "FBC02D",
            "F9A825",
            "F57F17",
            "FFA000",
            "FF8F00",
            "FF6F00",
            "F57C00",
            "EF6C00",
            "E65100",
            "E64A19",
            "D84315",
            "BF360C"
    };

}

package ru.alfabank.alfamir.utility.initials;

import com.google.common.base.Strings;

import javax.inject.Inject;

public class InitialsProviderImp implements InitialsProvider {

    @Inject
    InitialsProviderImp(){ }

    @Override
    public String formInitials(String name) {
        if(Strings.isNullOrEmpty(name)) return "";
        name = name.trim();
        String [] namePart = name.split(" ");
        int namePartCount = namePart.length;

        if(namePartCount == 1){
            return namePart[0].substring(0,1).toUpperCase();
        } else if (namePartCount == 2){
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(namePart[1].substring(0,1).toUpperCase());
            sBuilder.append(namePart[0].substring(0,1).toUpperCase());
            return sBuilder.toString();
        } else if (namePartCount > 2){
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(namePart[1].substring(0,1).toUpperCase());
            sBuilder.append(namePart[2].substring(0,1).toUpperCase());
            return sBuilder.toString();
        }
        return "";
    }
}

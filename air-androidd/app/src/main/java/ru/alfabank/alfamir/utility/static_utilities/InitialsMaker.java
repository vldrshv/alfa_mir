package ru.alfabank.alfamir.utility.static_utilities;

import javax.inject.Inject;

/**
 * Created by U_M0WY5 on 10.01.2018.
 */

public class InitialsMaker {

    @Inject
    public InitialsMaker(){}

    public static String formInitials(String fullName){
        if(fullName!=null&&!fullName.equals("")){
            fullName = fullName.trim();
            String [] initials = fullName.split(" ");
            if(initials.length>=2){
                try{
                    StringBuilder sBuilder = new StringBuilder();
                    for (int i = 1; i > -1; i--){
                        sBuilder.append(initials[i].substring(0,1));
                    }
                    return sBuilder.toString();
                } catch (Exception e){
                    return fullName.substring(0,1);
                }
            } else {
                return fullName.substring(0,1);
            }
        } else {
            return "";
        }
    }

    public String getInitials(String fullName){
        if(fullName!=null&&!fullName.equals("")){
            fullName = fullName.trim();
            String [] initials = fullName.split(" ");
            if(initials.length>=2){
                try{
                    StringBuilder sBuilder = new StringBuilder();
                    for (int i = 1; i > -1; i--){
                        sBuilder.append(initials[i].substring(0,1));
                    }
                    return sBuilder.toString();
                } catch (Exception e){
                    return fullName.substring(0,1);
                }
            } else {
                return fullName.substring(0,1);
            }
        } else {
            return "";
        }
    }

}

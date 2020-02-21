package ru.alfabank.alfamir.utility.static_utilities;

import android.text.Html;

/**
 * Created by mshvd_000 on 27.08.2017.
 */

public class HtmlWrapper {

    public static String convert(String html){
        if(html.length()==13&&html.indexOf("<p>&nbsp;</p>")==0){
            return "";
        } else {
            String text = Html.fromHtml(html).toString();
            text = text.replaceAll(" +", " ")
                    .replaceAll("[\r\n]+", "\n\n")
                    .replaceAll("\\u00A0", "");
            text = text.trim();
            return text;
        }
    }
}

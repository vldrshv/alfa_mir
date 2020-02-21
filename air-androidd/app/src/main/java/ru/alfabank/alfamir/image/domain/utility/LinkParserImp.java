package ru.alfabank.alfamir.image.domain.utility;

import javax.inject.Inject;

import ru.alfabank.alfamir.Constants;

public class LinkParserImp implements LinkParser {

    /**
     * Class returns pic url and parameters that match the screen size
     */

    @Inject
    LinkParserImp() {}

    @Override
    public ResponseValue getImageParameters(String url, int targetDp) {
        int targetPixelWidth = (int)(targetDp * Constants.Initialization.getDENSITY());
        String[] stringArray = url.split(";#");
        if(stringArray.length==0) return null;
        String formattedUrl = stringArray[0];
        if(stringArray.length==1) return new ResponseValue(formattedUrl, 0, targetPixelWidth);
        int width = Integer.parseInt(stringArray[1]);
        int height = Integer.parseInt(stringArray[2]);
        if(width > targetPixelWidth){
            float  coef = (float) targetPixelWidth / width;
            width = targetPixelWidth;
            height = (int)(height * coef);
        }
        return new ResponseValue(formattedUrl, width, height);
    }
}

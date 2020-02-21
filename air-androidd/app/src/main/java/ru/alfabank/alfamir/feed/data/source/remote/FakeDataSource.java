package ru.alfabank.alfamir.feed.data.source.remote;

import android.content.res.Resources;

import javax.inject.Inject;

public class FakeDataSource {

    private Resources mResources;

    @Inject
    FakeDataSource(Resources resources){
        mResources = resources;
    }

    public String getJson(){
//        InputStream is = mResources.openRawResource(R.raw.main_feed);
//        Writer writer = new StringWriter();
//        char[] buffer = new char[1024];
//        try {
//            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            int n;
//            while ((n = reader.read(buffer)) != -1) {
//                writer.write(buffer, 0, n);
//            }
//            is.close();
//        } catch (Exception e){
//
//        }
//        String jsonString = writer.toString();
        return "";
    }
}
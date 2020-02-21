package ru.alfabank.alfamir.alfa_tv.data.source.remote;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;

public class FakeDataSource {

    private Resources mResources;

    @Inject
    FakeDataSource(Resources resources){
        mResources = resources;
    }

    public String getJson(){
        InputStream is = mResources.openRawResource(R.raw.alfa_tv);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (Exception e){

        }
        String jsonString = writer.toString();
        return jsonString;
    }
}
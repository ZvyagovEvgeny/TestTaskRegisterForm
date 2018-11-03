package com.belina.registration.utils;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.belina.registration.R;

public class Constant  {


    private Context context;

    public Constant(Context context) {
        this.context = context;
    }

    private List<String> domains;

    public List<String> getDomains(){
        if(domains==null){
            String[] domains = context.getResources().getStringArray(R.array.popular_domains);
            this.domains = Arrays.asList(domains);
        }
        return this.domains;
    }

    public final static String IP_API_ADDRESS = "http://ip-api.com/";




}

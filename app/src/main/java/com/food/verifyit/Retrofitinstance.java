package com.food.verifyit;


import static com.food.verifyit.ScanDrinkActivity.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofitinstance {

        private static Retrofit retrofit = null;

    public static myAPI getDrinksAPI(){
        Gson gson = new GsonBuilder()
                .setLenient()  // Allow lenient parsing
                .create();



            if(retrofit == null){

                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }

            return retrofit.create(myAPI.class);
        }
    }


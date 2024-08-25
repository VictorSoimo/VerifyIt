package com.food.verifyit;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface myAPI {
    @GET("manufacturer_db/controllers/drinkoperations.php")
    Call<List<Drinkscanned>> getdrinkbycode( @Query("getdrinkbycode") String getdrinkbycode,
                                              @Query("drinkcode") String drinkcode);


}

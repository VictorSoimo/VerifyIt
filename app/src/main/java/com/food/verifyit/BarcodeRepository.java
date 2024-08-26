package com.food.verifyit;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class BarcodeRepository {
    private static BarcodeRepository instance;
    private final MutableLiveData<Drinkscanned> drinkLiveData = new MutableLiveData<>();

    private BarcodeRepository() {
    }

    public static synchronized BarcodeRepository getInstance() {
        if (instance == null) {
            instance = new BarcodeRepository();
        }
        return instance;
    }

    public void setDrink(Drinkscanned drink) {
        drinkLiveData.postValue(drink);
    }

    public LiveData<Drinkscanned> getDrinkLiveData() {
        return drinkLiveData;
    }

}

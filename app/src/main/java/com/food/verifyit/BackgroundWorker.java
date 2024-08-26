    package com.food.verifyit;

    import android.content.Context;
    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.work.Worker;
    import androidx.work.WorkerParameters;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    import java.util.List;

    public class BackgroundWorker extends Worker {

        private BarcodeRepository repository;
        private Drinkscanned drink1;

        public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
            repository = BarcodeRepository.getInstance();

        }


        @NonNull
        @Override
        public Result doWork() {
            drink1=new Drinkscanned(this.getApplicationContext());
            String drinkCode = getInputData().getString("drinkCode");
            if (drinkCode != null) {
                myAPI retrofitService = Retrofitinstance.getDrinksAPI();
                Call<List<Drinkscanned>> apiData = retrofitService.getdrinkbycode("true", drinkCode);

                apiData.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Drinkscanned>> call, @NonNull Response<List<Drinkscanned>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            List<Drinkscanned> drinks = response.body();

                           repository.setDrink(drinks.get(0));

                            Log.d("BarcodeWorker", "Drink details: " + drinks.get(0).toString());
                        } else {
                            Log.e("BarcodeWorker", "Error response: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Drinkscanned>> call, @NonNull Throwable t) {
                        Log.e("BarcodeWorker", "Request failed: " + t.getMessage());
                    }
                });
            }
            return Result.success();
        }
    }

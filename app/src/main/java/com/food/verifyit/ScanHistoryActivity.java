package com.food.verifyit;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ScanHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DrinkAdapter adapter;
    private Drinkscanned drink1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drinks);

        recyclerView = findViewById(R.id.recycler_view_drinks);
        drink1=new Drinkscanned(this);
        viewdrink();

    }
    public void viewdrink(){

        // Query the database
        List<Drinkscanned> drinks = drink1.getAllDrinks();

        // Set up the adapter
        adapter = new DrinkAdapter(this, drinks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

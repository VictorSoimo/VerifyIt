package com.food.verifyit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<Drinkscanned> drinkList;
    private Context context;

    public DrinkAdapter(Context context, List<Drinkscanned> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drinkscanned drink = drinkList.get(position);
        holder.drinkCode.setText(drink.getDrinkcode());
        holder.drinkName.setText(drink.getDrinkname());
        holder.manufacturer.setText(drink.getManufacturer());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = drink.getTimescanned().format(formatter);
        holder.scannedAt.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder {
        TextView drinkCode;
        TextView drinkName;
        TextView manufacturer;
        TextView scannedAt;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkCode = itemView.findViewById(R.id.drinkCode);
            drinkName = itemView.findViewById(R.id.drinkName);
            manufacturer = itemView.findViewById(R.id.manufacturer);
            scannedAt = itemView.findViewById(R.id.scannedAt);
        }
    }
}


package com.example.m_techcartuner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.R;
import com.example.m_techcartuner.model.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private OnCarClickListener onCarClickListener;
    private OnCarLongClickListener onCarLongClickListener;

    public interface OnCarClickListener {
        void onCarClick(Car car);
    }

    public interface OnCarLongClickListener {
        void onCarLongClicked(Car car);
    }

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    public CarAdapter(List<Car> carList, OnCarClickListener clickListener) {
        this.carList = carList;
        this.onCarClickListener = clickListener;
    }

    public void setOnCarLongClickListener(OnCarLongClickListener listener) {
        this.onCarLongClickListener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);

        holder.textMake.setText(car.getMake());
        holder.textModel.setText(car.getModel());
        holder.textYear.setText(String.valueOf(car.getYear()));
        holder.textEngine.setText(car.getEngine());

        if (onCarClickListener != null) {
            holder.itemView.setOnClickListener(v -> onCarClickListener.onCarClick(car));
        }

        if (onCarLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                onCarLongClickListener.onCarLongClicked(car);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView textMake, textModel, textYear, textEngine;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            textMake = itemView.findViewById(R.id.textMake);
            textModel = itemView.findViewById(R.id.textModel);
            textYear = itemView.findViewById(R.id.textYear);
            textEngine = itemView.findViewById(R.id.textEngine);
        }
    }
}

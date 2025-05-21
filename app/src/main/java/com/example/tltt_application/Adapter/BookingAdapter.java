package com.example.tltt_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tltt_application.R;
import com.example.tltt_application.View.BookingDetailActivity;
import com.example.tltt_application.databinding.ItemBookingBinding;
import com.example.tltt_application.objects.Booking;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingBinding binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.binding.dates.setText(String.format("12h00 T7, %s - 12h00 T7, %s", booking.getPickupDate(), booking.getReturnDate()));
        holder.binding.status.setText(booking.getStatus() == 1 ? "Đang thuê" : "Hoàn thành");
        holder.binding.status.setTextColor(booking.getStatus() == 1 ?
                context.getResources().getColor(android.R.color.holo_green_dark) :
                context.getResources().getColor(android.R.color.holo_green_light));
        holder.binding.totalPrice.setText(String.format("%,d đ", (long) booking.getTotalPrice()));

        if (booking.getCarImageUrl() != null) {
            Glide.with(holder.itemView.getContext()).load(booking.getCarImageUrl()).into(holder.binding.carImage);
        } else {
            holder.binding.carImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.binding.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookingBinding binding;

        public BookingViewHolder(ItemBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
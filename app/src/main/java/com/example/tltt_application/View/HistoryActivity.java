package com.example.tltt_application.View;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tltt_application.Adapter.BookingAdapter;
import com.example.tltt_application.R;
import com.example.tltt_application.ViewModel.BookingViewModel;
import com.example.tltt_application.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private BookingViewModel bookingViewModel;
    private BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo RecyclerView
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(this, new java.util.ArrayList<>());
        binding.recyclerViewHistory.setAdapter(bookingAdapter);

        // Khởi tạo ViewModel
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        // Lấy userId từ Intent
        String userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            // Gọi loadHistory từ ViewModel
            bookingViewModel.loadHistory(userId);

            // Quan sát LiveData để cập nhật RecyclerView
            bookingViewModel.getBookings().observe(this, bookings -> {
                if (bookings != null) {
                    bookingAdapter.updateBookings(bookings);
                    binding.recyclerViewHistory.setAdapter(bookingAdapter);
                }
            });
        } else {
            finish(); // Đóng Activity nếu userId không hợp lệ
        }
    }
}
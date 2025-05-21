package com.example.tltt_application.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.tltt_application.databinding.ActivityBookingDetailBinding;
import com.example.tltt_application.objects.Booking;
import com.example.tltt_application.ViewModel.BookingViewModel;

public class BookingDetailActivity extends AppCompatActivity {
    private ActivityBookingDetailBinding binding;
    private BookingViewModel bookingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        // Lấy dữ liệu booking từ intent và set cho ViewModel
        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking != null) {
            bookingViewModel.setBooking(booking);
        }

        // Quan sát booking data thay đổi và cập nhật UI
        bookingViewModel.getBooking().observe(this, b -> {
            if (b != null) {
                Glide.with(this).load(b.getCarImageUrl()).into(binding.carImage);
                binding.tvPickupDate.setText("Ngày nhận: " + (b.getPickupDate() != null ? b.getPickupDate() : "N/A"));
                binding.tvPickupTime.setText("Giờ nhận: " + (b.getPickupTime() != null ? b.getPickupTime() : "N/A"));
                binding.tvReturnDate.setText("Ngày trả: " + (b.getReturnDate() != null ? b.getReturnDate() : "N/A"));
                binding.tvReturnTime.setText("Giờ trả: " + (b.getReturnTime() != null ? b.getReturnTime() : "N/A"));
                binding.tvCity.setText("Thuê xe tại: " + (b.getCity() != null ? b.getCity() : "N/A"));
                binding.tvCarName.setText("Tên xe: " + (b.getCarName() != null ? b.getCarName() : "N/A"));
                binding.tvUserName.setText("Tên người dùng: " + (b.getUserName() != null ? b.getUserName() : "N/A"));
                binding.tvPhone.setText("Số điện thoại: " + (b.getPhone() != null ? b.getPhone() : "N/A"));
                binding.tvMethod.setText("Giấy tờ xác nhận: " + (b.getMethod() != null ? b.getMethod() : "N/A"));
                binding.tvStatus.setText("Trạng thái: " + (b.getStatus() == 1 ? "Đang thuê" : "Hoàn thành"));
                binding.tvTotalPrice.setText("Tổng giá: " + String.format("%,d đ", (long) b.getTotalPrice()));
            }
        });

        try {
            binding.btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("showFragment", "NotifyFragment");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            Log.e("BookingDetailActivity", "Lỗi khi thiết lập nút Quay lại: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi nút Quay lại: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

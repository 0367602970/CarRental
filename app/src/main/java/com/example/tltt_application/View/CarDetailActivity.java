package com.example.tltt_application.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tltt_application.Adapter.ImagePagerAdapter;
import com.example.tltt_application.Adapter.ThumbnailAdapter;
import com.example.tltt_application.R;
import com.example.tltt_application.ViewModel.CarDetailViewModel;
import com.example.tltt_application.databinding.ActivityCarDetailBinding;
import com.example.tltt_application.objects.Car;
import com.example.tltt_application.objects.User;
import com.google.gson.Gson;

public class CarDetailActivity extends AppCompatActivity {
    private ActivityCarDetailBinding binding;
    private CarDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sửa ở đây: khởi tạo ViewModel đúng cách trong Java
        viewModel = new ViewModelProvider(this).get(CarDetailViewModel.class);

        // Lấy User từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        User user;
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        } else {
            user = new User("Người dùng", "", "", "", "", "", "");
        }
        viewModel.setUser(user);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        Car car = (Car) intent.getSerializableExtra("car");
        if (car == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin xe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        viewModel.setCar(car);

        viewModel.setBookingInfo(
                intent.getStringExtra("pickupDate"),
                intent.getStringExtra("pickupTime"),
                intent.getStringExtra("returnDate"),
                intent.getStringExtra("returnTime"),
                intent.getStringExtra("city")
        );

        // Quan sát dữ liệu Car để cập nhật UI
        viewModel.getCar().observe(this, new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                binding.carName.setText(car.getName() != null ? car.getName() : "N/A");
                binding.carPrice.setText(viewModel.formatPrice(car.getPrice()));
                binding.carType.setText("Loại xe: " + (car.getTidy() != null ? car.getTidy() : "N/A"));
                binding.carRange.setText("Quãng đường: " + (car.getKind() != null ? car.getKind() : "N/A"));
                binding.carSeats.setText("Số chỗ: " + (car.getSeats() != null ? car.getSeats() : "N/A"));
                binding.carTrunk.setText("Dung tích cốp: " + (car.getTrunk() != null ? car.getTrunk() : "N/A"));

                ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(car.getImageUrls());
                binding.imageViewPager.setAdapter(imagePagerAdapter);

                ThumbnailAdapter thumbnailAdapter = new ThumbnailAdapter(car.getImageUrls(), position -> {
                    binding.imageViewPager.setCurrentItem(position, true);
                });
                binding.thumbnailRecyclerView.setLayoutManager(new LinearLayoutManager(CarDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                binding.thumbnailRecyclerView.setAdapter(thumbnailAdapter);

                binding.imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        thumbnailAdapter.setSelectedPosition(position);
                    }
                });
            }
        });

        // Đặt nút đặt xe
        binding.bookButton.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(CarDetailActivity.this, ConfirmActivity.class);
            confirmIntent.putExtra("pickupDate", viewModel.getPickupDate());
            confirmIntent.putExtra("pickupTime", viewModel.getPickupTime());
            confirmIntent.putExtra("returnDate", viewModel.getReturnDate());
            confirmIntent.putExtra("returnTime", viewModel.getReturnTime());
            confirmIntent.putExtra("city", viewModel.getCity());
            confirmIntent.putExtra("car", viewModel.getCar().getValue());
            confirmIntent.putExtra("user", viewModel.getUser().getValue());
            startActivity(confirmIntent);
        });
    }
}

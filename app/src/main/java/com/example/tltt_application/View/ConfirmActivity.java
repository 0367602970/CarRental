package com.example.tltt_application.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.ActivityConfirmBinding;
import com.example.tltt_application.objects.Car;
import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConfirmActivity extends AppCompatActivity {
    private ActivityConfirmBinding binding;
    private FirebaseFirestore db;
    private static final String TAG = "ConfirmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String pickupDate = intent.getStringExtra("pickupDate");
        String pickupTime = intent.getStringExtra("pickupTime");
        String returnDate = intent.getStringExtra("returnDate");
        String returnTime = intent.getStringExtra("returnTime");
        String city = intent.getStringExtra("city");
        Car car = (Car) intent.getSerializableExtra("car");

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        User user;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        } else {
            user = null;
        }

        if (user != null) {
            binding.edName.setText((user.getName() != null ? user.getName() : "N/A"));
            binding.edPhone.setText((user.getPhone() != null ? user.getPhone() : "N/A"));
        } else {
            binding.edName.setText("N/A");
            binding.edPhone.setText("N/A");
        }

        String searchInfoText = city != null ? city : "N/A";
        if (pickupDate != null && pickupTime != null && returnDate != null && returnTime != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date pickupDateTime = dateFormat.parse(pickupDate + " " + pickupTime);
                Date returnDateTime = dateFormat.parse(returnDate + " " + returnTime);

                long diffInMillies = returnDateTime.getTime() - pickupDateTime.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                searchInfoText += ", " + diffInDays + " ngày - " + pickupDate + " " + pickupTime + " - " + returnDate + " " + returnTime;
            } catch (Exception e) {
                e.printStackTrace();
                searchInfoText += ", N/A";
            }
        } else {
            searchInfoText += ", N/A";
        }
        binding.searchInfo.setText(searchInfoText);
        double totalPrice = 0.0;
        if (car != null) {
            binding.carName.setText(car.getName() != null ? car.getName() : "N/A");
            if (car.getImageUrl() != null) {
                Glide.with(this)
                        .load(car.getImageUrl())
                        .into(binding.carImage);
            } else {
                binding.carImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
            if (pickupDate != null && returnDate != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date pickup = dateFormat.parse(pickupDate);
                    Date returnD = dateFormat.parse(returnDate);
                    long diffInMillies = returnD.getTime() - pickup.getTime();
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    totalPrice = diffInDays * car.getPrice();
                    binding.totalPrice.setText(String.format("%,dđ", (long) totalPrice));
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.totalPrice.setText("N/A");
                }
            } else {
                binding.totalPrice.setText("N/A");
            }
        } else {
            binding.carName.setText("N/A");
            binding.carImage.setImageResource(R.drawable.ic_launcher_foreground);
            binding.totalPrice.setText("N/A");
        }

        Button confirmButton = binding.confirmButton;
        double finalTotalPrice = totalPrice;
        confirmButton.setOnClickListener(v -> {
            saveBookingToFirestore(pickupDate, pickupTime, returnDate, returnTime, city, car, user, finalTotalPrice);
        });

        // Chạy kiểm tra định kỳ
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndUpdateBookingStatus();
                // Lặp lại sau 24 giờ (86400000 milliseconds)
                new Handler().postDelayed(this, 86400000);
            }
        }, 0);
    }

    private void saveBookingToFirestore(String pickupDate, String pickupTime, String returnDate, String returnTime, String city, Car car, User user, double totalPrice) {
        if (pickupDate == null || pickupTime == null || returnDate == null || returnTime == null || city == null || car == null || user == null) {
            Toast.makeText(this, "Thông tin không đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy lựa chọn từ RadioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        String selectedMethod = selectedRadioButton.getText().toString();

        String userId = user.getPhone();

        // Tạo dữ liệu để lưu vào Firestore
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("pickupDate", pickupDate);
        bookingData.put("pickupTime", pickupTime);
        bookingData.put("returnDate", returnDate);
        bookingData.put("returnTime", returnTime);
        bookingData.put("city", city);
        bookingData.put("carName", car.getName());
        bookingData.put("carImageUrl", car.getImageUrl());
        bookingData.put("userName", user.getName());
        bookingData.put("phone", user.getPhone());
        bookingData.put("method", selectedMethod);
        bookingData.put("status", 1); // Trạng thái 1: Đang thuê
        bookingData.put("createdAt", new Date());
        bookingData.put("totalPrice", totalPrice);
        bookingData.put("userId", userId);

        // Lưu vào Firestore
        db.collection("booking")
                .add(bookingData)
                .addOnSuccessListener(documentReference -> {
                    showCustomSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi lưu booking: " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi khi đặt xe: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showCustomSuccessDialog() {
        // Tạo AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_book_success, null);
        builder.setView(dialogView);

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Loại bỏ viền mặc định
        dialog.setCanceledOnTouchOutside(false); // Không cho phép tắt khi chạm ra ngoài
        dialog.getWindow().setDimAmount(0.6f); // Làm mờ nền với độ trong suốt 60%
        dialog.show();

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss(); // Đóng dialog
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Phương thức để kiểm tra và cập nhật status (gọi định kỳ hoặc trong service)
    private void checkAndUpdateBookingStatus() {
        db.collection("booking")
                .whereEqualTo("status", 1) // Chỉ kiểm tra các booking đang thuê
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    Date currentDateTime = new Date();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String returnDate = document.getString("returnDate");
                        String returnTime = document.getString("returnTime");

                        if (returnDate != null && returnTime != null) {
                            try {
                                Date returnDateTime = dateFormat.parse(returnDate + " " + returnTime);
                                if (currentDateTime.after(returnDateTime)) {
                                    // Cập nhật status thành 2 nếu hết hạn
                                    document.getReference().update("status", 2)
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật status thành 2 thành công"))
                                            .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật status: " + e.getMessage(), e));
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Lỗi khi parse ngày giờ: " + e.getMessage(), e);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi lấy danh sách booking: " + e.getMessage(), e));
    }
}
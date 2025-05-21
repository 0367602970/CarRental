package com.example.tltt_application.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tltt_application.ViewModel.ChangePasswordViewModel;
import com.example.tltt_application.databinding.ActivityChangePasswordBinding;
import com.example.tltt_application.objects.User;
import com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private ChangePasswordViewModel viewModel;
    private String currentPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy thông tin user từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        User user = null;
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
        if (user == null || user.getPhone() == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentPhone = user.getPhone();

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        // Load mật khẩu hiện tại
        viewModel.loadCurrentPassword(currentPhone);

        observeViewModel();

        binding.btnConfirm.setOnClickListener(v -> {
            String currentPasswordInput = binding.edtCurrentPassword.getText().toString().trim();
            String newPassword = binding.edtNewPassword.getText().toString().trim();
            String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

            viewModel.changePassword(currentPhone, currentPasswordInput, newPassword, confirmPassword);
        });

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("showFragment", "AccountFragment");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getPasswordChangedLiveData().observe(this, changed -> {
            if (changed != null && changed) {
                // Xóa dữ liệu đăng nhập
                getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();

                // Điều hướng về LoginActivity
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

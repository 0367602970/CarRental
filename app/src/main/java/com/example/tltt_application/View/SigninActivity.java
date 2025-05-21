package com.example.tltt_application.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tltt_application.ViewModel.SigninViewModel;
import com.example.tltt_application.databinding.ActivitySigninBinding;

public class SigninActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;
    private SigninViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        observeViewModel();
        setupClickListeners();
    }

    private void observeViewModel() {
        viewModel.getMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSignupSuccessLiveData().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupClickListeners() {
        binding.btnSignup.setOnClickListener(v -> {
            String phone = binding.editPhone.getText().toString().trim();
            String name = binding.editName.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            String confirmPassword = binding.editConfirmPassword.getText().toString().trim();

            viewModel.signup(phone, name, password, confirmPassword);
        });

        binding.btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}

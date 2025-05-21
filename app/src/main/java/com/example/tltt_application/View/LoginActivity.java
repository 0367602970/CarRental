package com.example.tltt_application.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tltt_application.ViewModel.LoginViewModel;
import com.example.tltt_application.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Nếu đã đăng nhập thì chuyển đến MainActivity
        if (loginViewModel.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setupRegisterLink();
        setupLoginButton();
        observeViewModel();
    }

    // Xử lý sự kiện nhấn nút đăng nhập
    private void setupLoginButton() {
        binding.btnLogin.setOnClickListener(v -> {
            String phone = binding.editPhone.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            loginViewModel.login(phone, password);
        });
    }

    // Quan sát dữ liệu từ ViewModel
    private void observeViewModel() {
        loginViewModel.getLoginResult().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", user.getName());
                startActivity(intent);
                finish();
            }
        });
    }

    // Tạo liên kết nhấn vào "Đăng kí ngay"
    private void setupRegisterLink() {
        String text = "Bạn chưa có tài khoản? Đăng kí ngay";
        SpannableString spannableString = new SpannableString(text);

        int start = text.indexOf("Đăng kí ngay");
        int end = start + "Đăng kí ngay".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Chuyển sang màn hình đăng ký
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvRegister.setText(spannableString);
        binding.tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvRegister.setHighlightColor(android.graphics.Color.TRANSPARENT);
    }
}

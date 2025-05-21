package com.example.tltt_application.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tltt_application.R;
import com.example.tltt_application.ViewModel.MyAccountViewModel;
import com.example.tltt_application.objects.User;
import com.google.gson.Gson;

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "MyAccountActivity";

    private TextView userName, birthDate, gender, license, phone, email;
    private ImageView btnBack, btnEdit;

    private CardView editProfileCard;
    private EditText editName, editBirth, editGender;
    private Button btnSave;

    private MyAccountViewModel viewModel;
    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        // Ánh xạ giao diện
        userName = findViewById(R.id.user_name);
        birthDate = findViewById(R.id.birth_date);
        gender = findViewById(R.id.gender);
        license = findViewById(R.id.license);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        btnBack = findViewById(R.id.btnBackImage);
        btnEdit = findViewById(R.id.btnEditImage);

        editProfileCard = findViewById(R.id.edit_profile_card);
        editName = findViewById(R.id.edit_name);
        editBirth = findViewById(R.id.edit_birth);
        editGender = findViewById(R.id.edit_gender);
        btnSave = findViewById(R.id.btn_save);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        if (userJson.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        User user = new Gson().fromJson(userJson, User.class);
        if (user == null || user.getPhone() == null || user.getPhone().isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        userPhone = user.getPhone();

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);

        // Quan sát LiveData
        viewModel.getUserLiveData().observe(this, this::updateUIWithUser);
        viewModel.getErrorLiveData().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUpdateSuccessLiveData().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                editProfileCard.setVisibility(View.GONE);
                // Refresh user data
                viewModel.fetchUser(userPhone);
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        // Lấy dữ liệu user
        viewModel.fetchUser(userPhone);

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("showFragment", "AccountFragment");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Xử lý nút sửa
        btnEdit.setOnClickListener(v -> {
            editProfileCard.setVisibility(View.VISIBLE);
            editName.setText(userName.getText().toString());
            editBirth.setText(birthDate.getText().toString());
            editGender.setText(gender.getText().toString());
        });

        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString();
            String newBirth = editBirth.getText().toString();
            String newGender = editGender.getText().toString();

            if (newName.isEmpty() || newBirth.isEmpty() || newGender.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.updateUser(userPhone, newName, newBirth, newGender);
        });

        // Xử lý Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateUIWithUser(User user) {
        userName.setText(user.getName() != null ? user.getName() : "Không có tên");
        birthDate.setText(user.getBirth() != null ? user.getBirth() : "Không có ngày sinh");
        gender.setText(user.getGender() != null ? user.getGender() : "Không có giới tính");
        license.setText(user.getGPLX() != null ? user.getGPLX() : "Không có GPLX");
        phone.setText(user.getPhone() != null ? user.getPhone() : "Không có số điện thoại");
        email.setText("Chưa liên kết");
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

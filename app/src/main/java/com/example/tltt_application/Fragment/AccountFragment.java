package com.example.tltt_application.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tltt_application.View.ChangePasswordActivity;
import com.example.tltt_application.View.LoginActivity;
import com.example.tltt_application.View.MyAccountActivity;
import com.example.tltt_application.databinding.FragmentAccountBinding;


public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;

    public AccountFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        String name = "Người dùng"; // Giá trị mặc định
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name", "Người dùng");
        }
        binding.accountName.setText(name);

        // Xử lý đăng xuất
        logout();
        myAccount();
        changePassword();

        return binding.getRoot();
    }

    public void myAccount(){
        binding.myAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAccountActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    public void changePassword() {
        binding.changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    public void logout() {
        binding.btnLogout.setOnClickListener(v -> {
            // Tạo AlertDialog để xác nhận đăng xuất
            new AlertDialog.Builder(getActivity())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xử lý đăng xuất
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        // Chuyển về LoginActivity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish(); // Kết thúc MainActivity
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        // Đóng dialog, không làm gì
                        dialog.dismiss();
                    })
                    .setCancelable(false) // Không cho phép nhấn nút Back để thoát dialog
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding
    }
}
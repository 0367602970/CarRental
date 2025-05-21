package com.example.tltt_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SigninViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signupSuccessLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public LiveData<Boolean> getSignupSuccessLiveData() {
        return signupSuccessLiveData;
    }

    public void signup(String phone, String name, String password, String confirmPassword) {
        if (!validateInput(phone, name, password, confirmPassword)) {
            return;
        }

        // Kiểm tra số điện thoại tồn tại
        db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        messageLiveData.setValue("Số điện thoại đã tồn tại!");
                    } else {
                        Map<String, Object> user = new HashMap<>();
                        user.put("phone", phone);
                        user.put("name", name);
                        user.put("password", password);

                        db.collection("users")
                                .document(phone)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    messageLiveData.setValue("Đăng ký thành công!");
                                    signupSuccessLiveData.setValue(true);
                                })
                                .addOnFailureListener(e -> {
                                    messageLiveData.setValue("Lỗi khi đăng ký: " + e.getMessage());
                                });
                    }
                });
    }

    private boolean validateInput(String phone, String name, String password, String confirmPassword) {
        if (phone.isEmpty()) {
            messageLiveData.setValue("Vui lòng nhập số điện thoại!");
            return false;
        }
        if (!phone.matches("\\d{10}")) {
            messageLiveData.setValue("Số điện thoại phải có 10 chữ số!");
            return false;
        }
        if (name.isEmpty()) {
            messageLiveData.setValue("Vui lòng nhập tên!");
            return false;
        }
        if (password.isEmpty()) {
            messageLiveData.setValue("Vui lòng nhập mật khẩu!");
            return false;
        }
        if (password.length() <= 8) {
            messageLiveData.setValue("Mật khẩu phải dài hơn 8 ký tự!");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            messageLiveData.setValue("Mật khẩu không khớp!");
            return false;
        }
        return true;
    }
}

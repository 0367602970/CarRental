package com.example.tltt_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordChangedLiveData = new MutableLiveData<>();

    private String currentPasswordStored;

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public LiveData<Boolean> getPasswordChangedLiveData() {
        return passwordChangedLiveData;
    }

    public void loadCurrentPassword(String phone) {
        db.collection("users")
                .document(phone)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentPasswordStored = documentSnapshot.getString("password");
                    } else {
                        messageLiveData.setValue("Không tìm thấy người dùng");
                    }
                })
                .addOnFailureListener(e -> {
                    messageLiveData.setValue("Lỗi khi lấy dữ liệu: " + e.getMessage());
                });
    }

    public void changePassword(String phone, String currentPasswordInput, String newPassword, String confirmPassword) {
        if (currentPasswordStored == null) {
            messageLiveData.setValue("Chưa tải mật khẩu hiện tại, vui lòng thử lại sau");
            return;
        }

        if (currentPasswordInput.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLiveData.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!currentPasswordInput.equals(currentPasswordStored)) {
            messageLiveData.setValue("Mật khẩu hiện tại không đúng");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            messageLiveData.setValue("Mật khẩu mới và xác nhận mật khẩu không khớp");
            return;
        }

        if (newPassword.length() <= 9) {
            messageLiveData.setValue("Mật khẩu mới phải từ 9 ký tự trở lên");
            return;
        }

        db.collection("users")
                .document(phone)
                .update("password", newPassword)
                .addOnSuccessListener(unused -> {
                    messageLiveData.setValue("Đổi mật khẩu thành công");
                    passwordChangedLiveData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    messageLiveData.setValue("Lỗi khi đổi mật khẩu: " + e.getMessage());
                });
    }
}

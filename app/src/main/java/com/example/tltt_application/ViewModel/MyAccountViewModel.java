package com.example.tltt_application.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyAccountViewModel extends ViewModel {

    private static final String TAG = "MyAccountViewModel";
    private final FirebaseFirestore db;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccessLiveData = new MutableLiveData<>();

    public MyAccountViewModel() {
        db = FirebaseFirestore.getInstance();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getUpdateSuccessLiveData() {
        return updateSuccessLiveData;
    }

    public void fetchUser(String phone) {
        if (phone == null || phone.isEmpty()) {
            errorLiveData.setValue("Số điện thoại không hợp lệ");
            return;
        }
        db.collection("users").document(phone).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                userLiveData.setValue(user);
                            } else {
                                errorLiveData.setValue("Dữ liệu người dùng không hợp lệ");
                            }
                        } else {
                            errorLiveData.setValue("Không tìm thấy thông tin người dùng");
                        }
                    } else {
                        Exception e = task.getException();
                        errorLiveData.setValue("Lỗi lấy dữ liệu: " + (e != null ? e.getMessage() : "Unknown"));
                        Log.e(TAG, "Lỗi lấy dữ liệu Firestore", e);
                    }
                });
    }

    public void updateUser(String phone, String name, String birth, String gender) {
        if (phone == null || phone.isEmpty()) {
            errorLiveData.setValue("Số điện thoại không hợp lệ");
            return;
        }

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", name);
        updatedData.put("birth", birth);
        updatedData.put("gender", gender);

        db.collection("users").document(phone)
                .set(updatedData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    updateSuccessLiveData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorLiveData.setValue("Cập nhật thất bại!");
                    updateSuccessLiveData.setValue(false);
                });
    }
}

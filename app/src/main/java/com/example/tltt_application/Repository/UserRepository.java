package com.example.tltt_application.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class UserRepository {

    private final FirebaseFirestore db;
    private final SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        db = FirebaseFirestore.getInstance();
        sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
    }

    public void loginUser(String phone, String password, MutableLiveData<User> userLiveData, MutableLiveData<String> loginResult) {
        db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String storedPassword = document.getString("password");

                        if (storedPassword != null && storedPassword.equals(password)) {
                            User user = document.toObject(User.class);
                            userLiveData.setValue(user);
                            saveUserToPrefs(user);
                        } else {
                            loginResult.setValue("Sai mật khẩu!");
                        }
                    } else {
                        loginResult.setValue("Số điện thoại không tồn tại!");
                    }
                })
                .addOnFailureListener(e -> loginResult.setValue("Lỗi: " + e.getMessage()));
    }

    private void saveUserToPrefs(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userJson", new Gson().toJson(user));
        editor.putString("userName", user.getName());
        editor.putString("userId", user.getPhone());
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}

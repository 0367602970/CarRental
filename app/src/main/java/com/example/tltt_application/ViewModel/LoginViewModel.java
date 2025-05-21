package com.example.tltt_application.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tltt_application.Repository.UserRepository;
import com.example.tltt_application.objects.User;

public class LoginViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> loginResult = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void login(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            loginResult.setValue("Vui lòng nhập số điện thoại và mật khẩu!");
            return;
        }
        repository.loginUser(phone, password, userLiveData, loginResult);
    }

    public boolean isLoggedIn() {
        return repository.isLoggedIn();
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getLoginResult() {
        return loginResult;
    }
}

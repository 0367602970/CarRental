package com.example.tltt_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.objects.Car;
import com.example.tltt_application.objects.User;

public class CarDetailViewModel extends ViewModel {
    private final MutableLiveData<Car> carLiveData = new MutableLiveData<>();

    private String pickupDate, pickupTime, returnDate, returnTime, city;

    public void setCar(Car car) {
        carLiveData.setValue(car);
    }

    public LiveData<Car> getCar() {
        return carLiveData;
    }

    public void setBookingInfo(String pickupDate, String pickupTime, String returnDate, String returnTime, String city) {
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.city = city;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public String getCity() {
        return city;
    }

    public String formatPrice(int price) {
        if (price <= 0) {
            return "N/A";
        }
        return "Chỉ từ " + String.format("%,d", price) + " VNĐ/Ngày";
    }
}

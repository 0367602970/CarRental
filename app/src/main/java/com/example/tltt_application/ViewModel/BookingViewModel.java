package com.example.tltt_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.objects.Booking;

public class BookingViewModel extends ViewModel {
    private final MutableLiveData<Booking> bookingLiveData = new MutableLiveData<>();

    public void setBooking(Booking booking) {
        bookingLiveData.setValue(booking);
    }

    public LiveData<Booking> getBooking() {
        return bookingLiveData;
    }
}

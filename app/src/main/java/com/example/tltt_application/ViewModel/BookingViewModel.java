package com.example.tltt_application.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.objects.Booking;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingViewModel extends ViewModel {
    // LiveData cho danh sách bookings (dùng cho MyTripFragment và HistoryActivity)
    private final MutableLiveData<List<Booking>> bookings = new MutableLiveData<>();
    // LiveData cho một booking cụ thể (dùng cho BookingDetailActivity)
    private final MutableLiveData<Booking> booking = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Lấy danh sách bookings
    public LiveData<List<Booking>> getBookings() {
        return bookings;
    }

    // Lấy một booking cụ thể
    public LiveData<Booking> getBooking() {
        return booking;
    }

    // Đặt một booking cụ thể
    public void setBooking(Booking booking) {
        this.booking.setValue(booking);
    }

    public void loadMyTrips(String userId) {
        loadBookings(userId, 1); // status = 1 cho My Trips
    }

    public void loadHistory(String userId) {
        loadBookings(userId, 2); // status = 2 cho History
    }

    private void loadBookings(String userId, int status) {
        db.collection("booking")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", status)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> bookingList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking booking = document.toObject(Booking.class);
                        bookingList.add(booking);
                    }
                    bookings.setValue(bookingList);
                })
                .addOnFailureListener(e -> {
                    bookings.setValue(new ArrayList<>());
                    Log.e("BookingViewModel", "Lỗi khi tải dữ liệu: " + e.getMessage());
                });
    }
}

package com.example.tltt_application.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tltt_application.Adapter.BookingAdapter;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.FragmentHomeBinding;
import com.example.tltt_application.databinding.FragmentNotifyBinding;
import com.example.tltt_application.objects.Booking;
import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment {
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private FragmentNotifyBinding binding;
    private FirebaseFirestore db;
    private static final String TAG = "NotifyFragment";

    public NotifyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recycleViewMyTrip.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList);
        binding.recycleViewMyTrip.setAdapter(bookingAdapter);

        db = FirebaseFirestore.getInstance();

        // Lấy userId từ userJson trong SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        String userId = null;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            userId = user != null ? user.getPhone() : null;
        }

        if (userId != null) {
            loadMyTrips(userId);
        } else {
            Log.e(TAG, "Không tìm thấy userId");

        }

        return view;
    }

    private void loadMyTrips(String userId) {
        db.collection("booking")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", 1)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookingList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking booking = document.toObject(Booking.class);
                        bookingList.add(booking);
                    }
                    bookingAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi lấy My Trips: " + e.getMessage(), e));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
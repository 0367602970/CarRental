package com.example.tltt_application.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tltt_application.Adapter.BookingAdapter;
import com.example.tltt_application.R;
import com.example.tltt_application.View.HistoryActivity;
import com.example.tltt_application.ViewModel.BookingViewModel;
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
    private BookingViewModel bookingViewModel;
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

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        db = FirebaseFirestore.getInstance();

        // Lấy userId từ userJson trong SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        String userId;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            userId = user != null ? user.getPhone() : null;
        } else {
            userId = null;
        }

        if (userId != null) {
            // Gọi loadMyTrips từ ViewModel
            bookingViewModel.loadMyTrips(userId);

            // Quan sát LiveData để cập nhật RecyclerView
            bookingViewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
                if (bookings != null) {
                    bookingAdapter.updateBookings(bookings); // Thêm phương thức updateBookings vào BookingAdapter
                    binding.recycleViewMyTrip.setAdapter(bookingAdapter);
                } else {
                    Log.e(TAG, "Danh sách booking rỗng");
                }
            });
        } else {
            Log.e(TAG, "Không tìm thấy userId");
        }

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            intent.putExtra("userId", userId); // Truyền userId sang HistoryActivity
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
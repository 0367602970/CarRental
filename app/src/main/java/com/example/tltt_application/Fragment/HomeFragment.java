package com.example.tltt_application.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tltt_application.View.CarListActivity;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.FragmentHomeBinding;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Calendar calendar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo View Binding cho Fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Nhận name từ Bundle
        String name = "Người dùng"; // Giá trị mặc định
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name", "Người dùng");
        }

        // Hiển thị thông điệp chào mừng
        binding.nameWelcome.setText("Chào mừng, " + name);
        

        showTabDate();


        calendar = Calendar.getInstance();

        // Gắn sự kiện cho pickup date (TextView và ImageView)
        setupDatePicker(binding.pickupDate, binding.pickupDateIcon);
        setupTimePicker(binding.pickupTime, binding.pickupTimeIcon);

        // Gắn sự kiện cho return date (TextView và ImageView)
        setupDateReturn(binding.returnDate, binding.returnDateIcon);
        setupTimePicker(binding.returnTime, binding.returnTimeIcon);

        setupSearchButton();

        return binding.getRoot();
    }

    private void showTabDate() {
        // Hiển thị nội dung của tab_date
        binding.tabDateContent.setVisibility(View.VISIBLE);
    }


    private void setupDatePicker(TextView textView, ImageView imageView) {
        // Lấy ngày tháng năm hiện tại và hiển thị mặc định
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;

        // Định dạng ngày hiện tại
        String currentDate = day + "/" + (month + 1) + "/" + year;
        textView.setText(currentDate); // Đặt ngày hiện tại làm mặc định

        // Listener để mở DatePickerDialog khi bấm vào TextView hoặc ImageView
        View.OnClickListener dateClickListener = v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        textView.setText(date);
                    },
                    year, month, day);
            datePickerDialog.show();
        };

        textView.setOnClickListener(dateClickListener);
        imageView.setOnClickListener(dateClickListener);
    }

    private void setupTimePicker(TextView textView, ImageView imageView) {
        // Lấy giờ phút hiện tại và hiển thị mặc định
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int displayHour;
        int displayMinute;

        // Định dạng giờ hiện tại (ví dụ: 14:30)

        if (hour >= 7 && hour < 17) {
            displayHour = hour;
            displayMinute = minute;
        } else {
            displayHour = 10;
            displayMinute = 0;
        }
        String currentTime = String.format("%02d:%02d", displayHour, displayMinute);
        textView.setText(currentTime);

        // Listener để mở TimePickerDialog khi bấm vào TextView hoặc ImageView
        View.OnClickListener timeClickListener = v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
                    (view, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textView.setText(time);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        };

        textView.setOnClickListener(timeClickListener);
        imageView.setOnClickListener(timeClickListener);
    }

    private void setupDateReturn(TextView textView, ImageView imageView) {
        // Lấy ngày tháng năm hiện tại và hiển thị mặc định
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 2;

        // Định dạng ngày hiện tại
        String currentDate = day + "/" + (month + 1) + "/" + year;
        textView.setText(currentDate); // Đặt ngày hiện tại làm mặc định

        // Listener để mở DatePickerDialog khi bấm vào TextView hoặc ImageView
        View.OnClickListener dateClickListener = v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        textView.setText(date);
                    },
                    year, month, day);
            datePickerDialog.show();
        };

        textView.setOnClickListener(dateClickListener);
        imageView.setOnClickListener(dateClickListener);
    }

    private void setupSearchButton() {
        // Xử lý sự kiện bấm nút "Tìm xe" trong tab_date
        binding.btnSearchDate.setOnClickListener(v -> {
            String pickupDate = binding.pickupDate.getText().toString();
            String pickupTime = binding.pickupTime.getText().toString();
            String returnDate = binding.returnDate.getText().toString();
            String returnTime = binding.returnTime.getText().toString();

            Spinner city = binding.city;
            String selectedCity = city.getSelectedItem() != null ? city.getSelectedItem().toString() : "N/A";

            Intent intent = new Intent(getActivity(), CarListActivity.class);
            intent.putExtra("pickupDate", pickupDate);
            intent.putExtra("pickupTime", pickupTime);
            intent.putExtra("returnDate", returnDate);
            intent.putExtra("returnTime", returnTime);
            intent.putExtra("city", selectedCity);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
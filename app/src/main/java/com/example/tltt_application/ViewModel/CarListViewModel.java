package com.example.tltt_application.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.Repository.CarRepository;
import com.example.tltt_application.objects.Car;

import java.util.List;

public class CarListViewModel extends ViewModel {
    private CarRepository carRepository;
    private LiveData<List<Car>> carListLiveData;

    public CarListViewModel() {
        carRepository = new CarRepository();
        carListLiveData = carRepository.getCarList();
    }

    public LiveData<List<Car>> getCarList(){
        return carListLiveData;
    }
}

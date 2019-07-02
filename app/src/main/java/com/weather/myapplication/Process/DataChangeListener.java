package com.weather.myapplication.Process;

import com.weather.myapplication.Process.API.Model.CurrentResponse;
import com.weather.myapplication.Process.API.Model.ForeCastResponse;

public interface DataChangeListener {
    void onCurrentResponseReceived(CurrentResponse currentResponse);

    void onForeCastResponseReceived(ForeCastResponse foreCastResponse);

    void onError(String message);

}

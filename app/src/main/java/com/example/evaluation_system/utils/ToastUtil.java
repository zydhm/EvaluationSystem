package com.example.evaluation_system.utils;

import android.widget.Toast;

import com.example.evaluation_system.base.BaseApp;

public class ToastUtil {

    public static void shortToast(String content) {
        Toast.makeText(BaseApp.getContext(), content, Toast.LENGTH_SHORT).show();
    }
}

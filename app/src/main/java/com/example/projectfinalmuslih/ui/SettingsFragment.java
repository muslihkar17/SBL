package com.example.projectfinalmuslih.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.example.projectfinalmuslih.R;
import com.example.projectfinalmuslih.SharedPrefManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private SwitchMaterial themeSwitch;
    private SharedPrefManager sharedPrefManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        themeSwitch = view.findViewById(R.id.theme_switch);

        // Atur status switch sesuai preferensi yang tersimpan
        themeSwitch.setChecked(sharedPrefManager.isDarkMode());

        // Tambahkan listener untuk menyimpan perubahan
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPrefManager.setDarkMode(isChecked); // Simpan preferensi
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}
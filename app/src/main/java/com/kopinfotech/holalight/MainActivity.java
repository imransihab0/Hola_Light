package com.kopinfotech.holalight;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kopinfotech.holalight.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            if (binding.button.getText().toString().equals("Turn on")) {
                binding.button.setText(R.string.turnoff);
                binding.flashimage.setImageResource(R.drawable.onimage);
                changeLightState(true);
            } else {
                binding.button.setText(R.string.turnon);
                binding.flashimage.setImageResource(R.drawable.offimage);
                changeLightState(false);
            }
        });
    }

    private void changeLightState(boolean state) {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        String camId;

        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length > 0) {
                camId = cameraIdList[0];
                if (isFlashAvailable(cameraManager, camId)) {
                    cameraManager.setTorchMode(camId, state);
                } else {
                    // Handle the case where the camera does not have a flash
                    showPopupMessage("Your device does not have a flash");
                    // You may want to show a message or disable the feature
                }
            } else {
                // Handle the case where there are no available cameras
                showPopupMessage("No available cameras on your device");
                // You may want to show a message or disable the feature
            }
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPopupMessage(String message) {
        // You can display a popup message using an AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", null); // You can add a listener if needed
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isFlashAvailable(CameraManager cameraManager, String cameraId) {
        try {
            return Boolean.TRUE.equals(cameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.button.setText(R.string.turnon);
    }


}

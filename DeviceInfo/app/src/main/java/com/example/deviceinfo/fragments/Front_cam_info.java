package com.example.deviceinfo.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.General_info_Adapter;
import com.example.deviceinfo.databinding.FragmentFrontCamInfoBinding;
import com.example.deviceinfo.models.General_info_Model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Front_cam_info extends Fragment {
    FragmentFrontCamInfoBinding binding;
    ArrayList<General_info_Model> info_models;
    Context context;
    General_info_Adapter general_info_adapter;
    android.hardware.Camera camera = null;
    Camera.CameraInfo cameraInfo;
    public Front_cam_info() {

    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        general_info_adapter=new General_info_Adapter(info_models,context);
        binding.frontcamRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.frontcamRecycler.setAdapter(general_info_adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info_models=new ArrayList<>();
        info_models.add(new General_info_Model(getResources().getString(R.string.resolution),getfrontCameraResolutionInMp()));
        info_models.add(new General_info_Model(getResources().getString(R.string.flash),hasFlash()+""));
        info_models.add(new General_info_Model(getResources().getString(R.string.focusModes),getSupportedFocusModes()));
        info_models.add(new General_info_Model(getResources().getString(R.string.sceneModes),getSupportedSceneModes()));
        info_models.add(new General_info_Model(getResources().getString(R.string.FocalLenght),camera.getParameters().getFocalLength()+" mm"));
        info_models.add(new General_info_Model(getResources().getString(R.string.AntiBanding),getSupportedAntibaningModes()));
        info_models.add(new General_info_Model(getResources().getString(R.string.thumbnailSize),getSupportedThumbnailSizes()));
        info_models.add(new General_info_Model(getResources().getString(R.string.imageRes),getSupportedImageResolutions()));
        info_models.add(new General_info_Model(getResources().getString(R.string.videoRes),getSupportedVideoResolutions()));
        camera.release();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFrontCamInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    public String getfrontCameraResolutionInMp()
    {
        String cameraHeightWidth = null;

        int noOfCameras = android.hardware.Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0;i < noOfCameras;i++)
        {
            cameraInfo= new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                camera = android.hardware.Camera.open(i);;
                android.hardware.Camera.Parameters cameraParams = camera.getParameters();

                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount)
                    {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
                    }
                }
                cameraHeightWidth =getCameraHeightWidth(camera);

            }
        }


        return String.format("%.1f", maxResolution)+"MP "+cameraHeightWidth;
    }
    public String getCameraHeightWidth(Camera camera){

        android.hardware.Camera.Parameters parameters = camera.getParameters();
        android.hardware.Camera.Size size = parameters.getPictureSize();


        int height = size.height;
        int width = size.width;

        return "("+width+" X "+height+")";

    }

    public boolean hasFlash() {
        if (camera == null) {
            return false;
        }

        Camera.Parameters parameters;
        try {
            parameters = camera.getParameters();
        } catch (RuntimeException ignored)  {
            return false;
        }

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }

    public String getSupportedFocusModes(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedFocusModes().size();i++){
            flashModes+=camera.getParameters().getSupportedFocusModes().get(i)+"\n";
        }
        return flashModes;
    }
    public String getSupportedSceneModes(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedSceneModes().size();i++){
            flashModes+=camera.getParameters().getSupportedSceneModes().get(i)+"\n";
        }
        return flashModes;
    }
    public String getSupportedAntibaningModes(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedAntibanding().size();i++){
            flashModes+=camera.getParameters().getSupportedAntibanding().get(i)+"\n";
        }
        return flashModes;
    }
    public String getSupportedThumbnailSizes(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedJpegThumbnailSizes().size();i++){
            flashModes+=camera.getParameters().getSupportedJpegThumbnailSizes().get(i).height+" x "+camera.getParameters().getSupportedJpegThumbnailSizes().get(i).width+"\n";
        }
        return flashModes;
    }
    public String getSupportedImageResolutions(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedPictureSizes().size();i++){
            flashModes+=camera.getParameters().getSupportedPictureSizes().get(i).height+" x "+camera.getParameters().getSupportedPictureSizes().get(i).width+"\n";
        }
        return flashModes;
    }
    public String getSupportedVideoResolutions(){
        String flashModes = "";
        for(int i=0;i<camera.getParameters().getSupportedVideoSizes().size();i++){
            flashModes+=camera.getParameters().getSupportedVideoSizes().get(i).height+" x "+camera.getParameters().getSupportedVideoSizes().get(i).width+"\n";
        }
        return flashModes;
    }
}
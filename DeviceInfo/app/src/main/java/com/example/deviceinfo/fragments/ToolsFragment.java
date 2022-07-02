package com.example.deviceinfo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.activities.Activity_Info_General;
import com.example.deviceinfo.activities.Battery_Info;
import com.example.deviceinfo.activities.Camera_info;
import com.example.deviceinfo.activities.System_info;
import com.example.deviceinfo.adapter.ToolsAdapter;
import com.example.deviceinfo.databinding.FragmentToolsBinding;
import com.example.deviceinfo.interfaces.OntoolsItemClicked;
import com.example.deviceinfo.models.Tools_Model;

import java.util.ArrayList;

public class ToolsFragment extends Fragment {
    FragmentToolsBinding binding;
    Context context;
    ArrayList<Tools_Model> tools;
    ToolsAdapter toolsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.context=context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tools=new ArrayList<>();
        tools.add(new Tools_Model(context.getResources().getString(R.string.device),R.drawable.ic_device));
        tools.add(new Tools_Model(context.getResources().getString(R.string.system),R.drawable.ic_system));
        tools.add(new Tools_Model(context.getResources().getString(R.string.display),R.drawable.ic_display));
        tools.add(new Tools_Model(context.getResources().getString(R.string.camera),R.drawable.ic_camera));
        tools.add(new Tools_Model(context.getResources().getString(R.string.battery),R.drawable.ic_batery_tools));
        tools.add(new Tools_Model(context.getResources().getString(R.string.simcard),R.drawable.ic_sim_card));
        tools.add(new Tools_Model(context.getResources().getString(R.string.sensor),R.drawable.ic_sensors_tools));
        tools.add(new Tools_Model(context.getResources().getString(R.string.blutooth),R.drawable.ic_blutooth_tools));
        tools.add(new Tools_Model(context.getResources().getString(R.string.network),R.drawable.ic_network));
        tools.add(new Tools_Model(context.getResources().getString(R.string.cpu),R.drawable.ic_cpu));
        tools.add(new Tools_Model(context.getResources().getString(R.string.storage),R.drawable.ic_storage));
        tools.add(new Tools_Model(context.getResources().getString(R.string.test),R.drawable.ic_tests));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding=FragmentToolsBinding.inflate(inflater,parent,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolsAdapter=new ToolsAdapter(tools,context);
        binding.recyclertools.setLayoutManager(new GridLayoutManager(context,3));
        binding.recyclertools.setAdapter(toolsAdapter);

        toolsAdapter.onToolsItemClicked(new OntoolsItemClicked() {
            @Override
            public void onToolsitemClicked(int position) {
                if(position==0){
                    Intent intent=new Intent(context, Activity_Info_General.class);
                    startActivity(intent);
                }else if(position==1){
                    Intent intent=new Intent(context, System_info.class);
                    startActivity(intent);
                }else if(position==2){

                }else if(position==3){
                    Intent intent=new Intent(context, Camera_info.class);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent=new Intent(context, Battery_Info.class);
                    startActivity(intent);
                }else if(position==5){

                }else if(position==6){

                }else if(position==7){

                }else if(position==8){

                }else if(position==9){

                }else if(position==10){

                }else if(position==11){

                }
            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
}
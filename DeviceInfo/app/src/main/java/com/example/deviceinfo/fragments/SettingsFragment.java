package com.example.deviceinfo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.ToolsAdapter;
import com.example.deviceinfo.databinding.FragmentSettingsBinding;
import com.example.deviceinfo.interfaces.OntoolsItemClicked;
import com.example.deviceinfo.models.Tools_Model;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    Context context;
    ArrayList<Tools_Model> tools;
    ToolsAdapter toolsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.context=context;
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tools=new ArrayList<>();
        tools.add(new Tools_Model(context.getResources().getString(R.string.lighttheme),R.drawable.ic_light_theme));
        tools.add(new Tools_Model(context.getResources().getString(R.string.languages),R.drawable.ic_translate));
        tools.add(new Tools_Model(context.getResources().getString(R.string.about),R.drawable.ic_about));
        tools.add(new Tools_Model(context.getResources().getString(R.string.rateapp),R.drawable.ic_like_rate));


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding=FragmentSettingsBinding.inflate(inflater,parent,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolsAdapter=new ToolsAdapter(tools,context);
        binding.recyclerSettings.setLayoutManager(new GridLayoutManager(context,2));
        binding.recyclerSettings.setAdapter(toolsAdapter);

        toolsAdapter.onToolsItemClicked(new OntoolsItemClicked() {
            @Override
            public void onToolsitemClicked(int position) {

            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
}
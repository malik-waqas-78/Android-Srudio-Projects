package com.ppt.walkie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ppt.walkie.R;


public class Intro_FragmentOKRA extends Fragment {
    String Title, Desc;
    int sliderImageID;

    public Intro_FragmentOKRA() {
    }

    public Intro_FragmentOKRA(String title, String desc, int sliderImageID) {
        Title = title;
        Desc = desc;
        this.sliderImageID = sliderImageID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.intro_fragment_okra, container, false);
        ImageView imageViewSlider = root.findViewById(R.id.imageViewSlider);
        TextView textViewTitle = root.findViewById(R.id.textViewTitle);
        TextView textViewDescription = root.findViewById(R.id.textViewDescription);

        imageViewSlider.setImageResource(sliderImageID);
        textViewTitle.setText(Title);
        textViewDescription.setText(Desc);
        return root;
    }
}

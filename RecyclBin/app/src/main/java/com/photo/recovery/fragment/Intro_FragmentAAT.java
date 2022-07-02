package com.photo.recovery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.photo.recovery.R;


public class Intro_FragmentAAT extends Fragment {
    String Title, Desc;
    int sliderImageID;
    int backGroundID;

    public Intro_FragmentAAT() {
    }

    public Intro_FragmentAAT(String title, String desc, int sliderImageID,int backId) {
        Title = title;
        Desc = desc;
        this.sliderImageID = sliderImageID;
        this.backGroundID=backId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.intro_fragment_aat, container, false);
        ImageView imageViewSlider = root.findViewById(R.id.imageViewSlider);
        TextView textViewTitle = root.findViewById(R.id.textViewTitle);
        TextView textViewDescription = root.findViewById(R.id.textViewDescription);
        ImageView background=root.findViewById(R.id.background);

        imageViewSlider.setImageResource(sliderImageID);
        background.setBackgroundResource(backGroundID);
        textViewTitle.setText(Title);
        textViewDescription.setText(Desc);
        return root;
    }
}

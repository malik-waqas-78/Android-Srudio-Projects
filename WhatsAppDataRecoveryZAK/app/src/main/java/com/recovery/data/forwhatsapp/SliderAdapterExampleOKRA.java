package com.recovery.data.forwhatsapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExampleOKRA extends SliderViewAdapter<SliderAdapterExampleOKRA.SliderAdapterVH> {
    private Context context;
    private List<SliderItemOKRA> mSliderItemOKRAS = new ArrayList<>();

    public SliderAdapterExampleOKRA(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItemOKRA> sliderItemOKRAS) {
        this.mSliderItemOKRAS = sliderItemOKRAS;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItemOKRAS.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItemOKRA sliderItemOKRA) {
        this.mSliderItemOKRAS.add(sliderItemOKRA);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterExampleOKRA.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_okra, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterExampleOKRA.SliderAdapterVH viewHolder, int position) {
        SliderItemOKRA sliderItemOKRA = mSliderItemOKRAS.get(position);

        viewHolder.textViewDescription.setText("Testing");
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(context)
                .load(sliderItemOKRA.getImg())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItemOKRAS.size();
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}

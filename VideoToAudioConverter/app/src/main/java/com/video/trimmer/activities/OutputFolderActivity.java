package com.video.trimmer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.video.trimmer.R;
import com.video.trimmer.adapters.FolderAdpater;
import com.video.trimmer.databinding.ActivityOutputFolderBinding;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.modelclasses.FolderModel;
import com.video.trimmer.utils.Constants;

import java.util.ArrayList;
import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;
public class OutputFolderActivity extends AppCompatActivity {
    ActivityOutputFolderBinding binding;
    FolderAdpater folderAdpater;
    ArrayList<FolderModel> folderModels=new ArrayList<>();
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOutputFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setItemsInArray();
    }
    private void setItemsInArray() {
        folderModels.add(new FolderModel(getResources().getString(R.string.covertedAudio),constants.CONVERTED_AUDIO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.trimmedAudios),constants.TRIMMED_AUDIO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.trimmedVideo),constants.TRIMMED_VIDEO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.removeAudioFolder),constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.audioaddedFolder),constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.createvideo),constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH));

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        folderAdpater=new FolderAdpater(this,folderModels);
        binding.outputsRecycler.setLayoutManager(layoutManager);
        binding.outputsRecycler.setAdapter(folderAdpater);

        folderAdpater.onFolderItemClicked(new OnFolderClicked() {
            @Override
            public void onFolderClicked(String path, int pos) {
                if(pos==0){
                    Intent intent=new Intent(OutputFolderActivity.this,OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY,1);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.covertedAudio));
                    startActivity(intent);
                }else if(pos==1){
                    Intent intent=new Intent(OutputFolderActivity.this,OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY,3);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.trimmedAudios));
                    startActivity(intent);
                }else if(pos==2){
                    Intent intent=new Intent(OutputFolderActivity.this,OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY,2);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.trimmedVideo));
                    startActivity(intent);
                }else if(pos==3){
                    Intent intent=new Intent(OutputFolderActivity.this,OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY,4);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.removeaudio));
                    startActivity(intent);
                }else if(pos==4){
                    Intent intent=new Intent(OutputFolderActivity.this,OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY,5);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.audioaddedFolder));
                    startActivity(intent);
                }else if(pos==5){

                }
            }
        });
    }
}
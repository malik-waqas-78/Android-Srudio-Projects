package com.niazitvpro.official.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.activity.SplashScreenActivity;
import com.niazitvpro.official.adapter.SubCategoryAdapter;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.model.MenuItemChannel;
import com.niazitvpro.official.model.SearchItemsModel;
import com.niazitvpro.official.utils.ItemOffsetDecoration;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RetrofitUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niazitvpro.official.activity.SplashScreenActivity.isNetworkAvailable;
import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryFragment extends Fragment {

    private SharedPrefTVApp sharedPrefTVApp;
    private FastScroller fastScroller;
    int appBackgroundColor;
    private RecyclerView recycler_subcategoryList;
    private ProgressBar progressBar;
    private FrameLayout ll_main;
    private SubCategoryAdapter adapter;
    private TextView tv_no_channel;
    public static String categoryId="";


    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        recycler_subcategoryList = view.findViewById(R.id.sub_category_item_list);
        progressBar = view.findViewById(R.id.progress_subcategory);
        tv_no_channel = view.findViewById(R.id.tv_no_channel);
        ll_main = view.findViewById(R.id.ll_main);
        fastScroller = view.findViewById(R.id.fast_scroll);
        fastScroller.setRecyclerView(recycler_subcategoryList);

        ll_main.setBackgroundColor(appBackgroundColor);
        categoryId = getArguments().getString("category_id");

        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Subcategory"+categoryId);
        if(getCacheResponse!=null && !getCacheResponse.equals("")){

            setRecycler_categoryItemList(getCacheResponse);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        getMenuCategoryChannelList(categoryId,false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },100);

        }else {
            try {
                SharedPrefTVApp.runTimer(getActivity(),progressBar);
                progressBar.setVisibility(View.VISIBLE);
                new ReadWriteJsonFileUtils(getContext()).deleteFile("Subcategory"+categoryId);
                getMenuCategoryChannelList(categoryId,true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getMenuCategoryChannelList(String categoryID,boolean isCacheNull) throws JSONException {

        if(!isNetworkAvailable(getActivity())){

            String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Subcategory"+categoryID);

            if(getCacheResponse!=null){

                setRecycler_categoryItemList(getCacheResponse);
            }
            progressBar.setVisibility(View.GONE);


        }else {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getActivity()).getMenuItemsCategoryList(categoryID);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String str_response = null;
                        try {
                            str_response = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        saveDataInCache(str_response);

//                        checkIfUpdateAVailable(str_response,categoryID);
                        setRecycler_categoryItemList(str_response);

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            });

        }


    }


    private  void setRecycler_categoryItemList(String jsonResponse){

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(jsonResponse);
            String status = jsonObject.getString("status");

            if (jsonObject.has("msg")) {

                String message = jsonObject.getString("msg");
                tv_no_channel.setVisibility(View.VISIBLE);
                tv_no_channel.setText(message);
                progressBar.setVisibility(View.GONE);
                if(adapter!=null){

                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }


            } else {

                tv_no_channel.setVisibility(View.GONE);

            }

            if (status.equals("1")) {

                JSONArray data = jsonObject.getJSONArray("data");
                MenuItemChannel menuItemChannel = new MenuItemChannel(data);
                menuItemChannel.menuItemChannelList.add(null);

                if(adapter!=null){
                    adapter.swap(menuItemChannel.menuItemChannelList);
                }else {
                    recycler_subcategoryList.setHasFixedSize(true);
                    adapter = new SubCategoryAdapter(getActivity(), menuItemChannel.menuItemChannelList);
                    adapter.setHasStableIds(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2,RecyclerView.VERTICAL,false);
                    recycler_subcategoryList.setLayoutManager(gridLayoutManager);
                    recycler_subcategoryList.setAdapter(adapter);

                    if (recycler_subcategoryList.getItemDecorationCount() == 0) {

                        recycler_subcategoryList.addItemDecoration(new ItemOffsetDecoration(2, 20, true));

                    }


                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return (position == menuItemChannel.menuItemChannelList.size()-1 ? 2 : 1);
                        }
                    });

                    RecyclerView.ItemAnimator animator = recycler_subcategoryList.getItemAnimator();

                    if (animator instanceof SimpleItemAnimator) {
                        ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                    }

                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

        } catch (JSONException e) {
            e.printStackTrace();

            progressBar.setVisibility(View.GONE);

        }


    }

    private void saveDataInCache(String response) {
        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Subcategory"+categoryId);
        if(getCacheResponse!=null && !getCacheResponse.equals("")){
            if(!getCacheResponse.equals(response)){
                try {
                    new ReadWriteJsonFileUtils(getContext()).createJsonFileData("Subcategory"+categoryId, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SearchItemsModel.setSearchList(getActivity(),categoryId,true);
            }
        }else {
            try {
                new ReadWriteJsonFileUtils(getContext()).createJsonFileData("Subcategory"+categoryId, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SearchItemsModel.setSearchList(getActivity(),categoryId,false);
        }
    }

}

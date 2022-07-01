package com.niazitvpro.official.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.SplashScreenActivity;
import com.niazitvpro.official.adapter.MenuItemChannelAdapter;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.model.CategoryItemModel;
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


public class MenuCategoryItemsFragment extends Fragment {

    private RecyclerView recycler_categoryItemList;
    private MenuItemChannelAdapter adapter;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private NestedScrollView ll_main;
    private TextView tv_no_channel;
    public  String categoryId="";
    private ProgressBar progressBar;

    public MenuCategoryItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_menu_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_home);

        initView(view);
    }

    private void initView(View view) {

        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        recycler_categoryItemList = view.findViewById(R.id.menu_category_item_list);
//        fastScroller = view.findViewById(R.id.fast_scroll);
//        fastScroller.setRecyclerView(recycler_categoryItemList);
        tv_no_channel = view.findViewById(R.id.tv_no_channel);
        ll_main = view.findViewById(R.id.ll_menu_item_category_list);

        ll_main.setBackgroundColor(appBackgroundColor);
        categoryId = getArguments().getString("menu_id");

        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Category"+categoryId);
             if(getCacheResponse!=null && !getCacheResponse.equals("")){

                 setRecycler_categoryItemList(getCacheResponse);

                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             getMenuCategoryChannelList(categoryId);
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                     }
                 },100);

             }else {

                 try {

                     SharedPrefTVApp.runTimer(getActivity(),progressBar);
                     progressBar.setVisibility(View.VISIBLE);
                     getMenuCategoryChannelList(categoryId);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }


             }

    }

    private void getMenuCategoryChannelList(String categoryID) throws JSONException {

        if(!SplashScreenActivity.isNetworkAvailable(getActivity())){

            String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Category"+categoryID);
            if(getCacheResponse!=null){

                setRecycler_categoryItemList(getCacheResponse);

            }
            progressBar.setVisibility(View.GONE);

        }else {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getActivity()).getSubcategoryList(categoryID);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                            try {
                                    new ReadWriteJsonFileUtils(getContext()).createJsonFileData("Category"+categoryID, str_response);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                            checkIfUpdateAVailable(str_response,categoryID);
                                setRecycler_categoryItemList(str_response);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
                tv_no_channel.setText("Category not found.");
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
                CategoryItemModel categoryItemModel = new CategoryItemModel(data);

                categoryItemModel.categoryItemList.add(null);

                if(adapter!=null){

                    adapter.swap(categoryItemModel.categoryItemList);

                }else {

                    recycler_categoryItemList.setHasFixedSize(true);
                    adapter = new MenuItemChannelAdapter(getActivity(), categoryItemModel.categoryItemList);
                    adapter.setHasStableIds(true);
                    CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);
                    customLayoutManager.removeAllViews();
                    recycler_categoryItemList.setLayoutManager(customLayoutManager);
                    recycler_categoryItemList.setAdapter(adapter);
                    ViewCompat.setNestedScrollingEnabled(recycler_categoryItemList, false);
                    recycler_categoryItemList.setItemAnimator(null);
                    if (recycler_categoryItemList.getItemDecorationCount() == 0) {

                        recycler_categoryItemList.addItemDecoration(new ItemOffsetDecoration(2, 20, true));

                    }
                    customLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return (position == categoryItemModel.categoryItemList.size()-1 ? 2 : 1);
                        }
                    });

                    RecyclerView.ItemAnimator animator = recycler_categoryItemList.getItemAnimator();

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

    public class CustomLinearLayoutManager extends GridLayoutManager {
        public CustomLinearLayoutManager(Context context,int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount,orientation, reverseLayout);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    private void checkIfUpdateAVailable(String response,String categoryID){
        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Category"+categoryID);
        if(getCacheResponse!=null && !getCacheResponse.equals(response)){
            setRecycler_categoryItemList(getCacheResponse);
        }else if(getCacheResponse.equals("")){
            setRecycler_categoryItemList(response);
        }
    }


}

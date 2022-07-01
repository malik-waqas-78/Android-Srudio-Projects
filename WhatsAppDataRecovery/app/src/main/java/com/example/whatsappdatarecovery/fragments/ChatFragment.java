package com.example.whatsappdatarecovery.fragments;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.ChatAdapter;
import com.example.whatsappdatarecovery.database.MyHelperClass;
import com.example.whatsappdatarecovery.modelclass.ModelClass;
import java.util.ArrayList;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    /*MyRealmHelper helper;*/
    MyHelperClass myHelperClass;
    Realm realm;
     ChatAdapter adapterRecyclerView;
    TextView textView_chat_data;
    ArrayList<ModelClass> itemslist=new ArrayList<>();
    ArrayList<ModelClass> toreverse;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        textView_chat_data=view.findViewById(R.id.text_chat_data);
        textView_chat_data.setText(R.string.nodata_found_inthis_category);
        setRecyclerviewAdapter();
        return view;
    }
    private void setRecyclerviewAdapter() {
        /*Realm.init(context);
        final Realm realm = Realm.getInstance(getDefaultConfig());
        helper = new MyRealmHelper(realm, getContext());
        toreverse = helper.retrievedata();
        getreverselist();*/
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        myHelperClass = new MyHelperClass(realm);
        myHelperClass.SelectFromDB();
        toreverse = myHelperClass.justRefresh();
        getreverselist();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRecyclerView = new ChatAdapter(getContext(), itemslist/*, realm*/);
        recyclerView.setAdapter(adapterRecyclerView);
        if (itemslist.size()==0)
        {
            textView_chat_data.setVisibility(View.VISIBLE);
        }
    }
    private void getreverselist() {
        for(int i=toreverse.size()-1;i>=0;i--){
            itemslist.add(toreverse.get(i));
        }
    }
   /* @Override
    public void deletedata(int position) {
        helper.deletedata(position);
    }*/
}
package com.test.aasanload.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.test.aasanload.R;
import com.test.aasanload.interfaces.MyAdapterCallBacks;
import com.test.aasanload.modelclasses.CodeModelClass;

import java.util.ArrayList;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MyAdapter.kt */
public final class MyAdapter extends Adapter<MyAdapter.MyViewHolder> {
    private final MyAdapterCallBacks callbacks;
    private ArrayList<CodeModelClass> codeArray;
    private Context mContext;

    /* compiled from: MyAdapter.kt */
    public final class MyViewHolder extends ViewHolder {
        private final ImageView iv_copy;
        private final RadioButton rb_select;
        private final TextView tv_code;

        public MyViewHolder( View view) {
            super(view);
            rb_select= view.findViewById(R.id.rb_select);
            tv_code = view.findViewById(R.id.tv_code);
            iv_copy = view.findViewById(R.id.iv_copy);
        }

    }

    public MyAdapter(Context context, ArrayList<CodeModelClass> arrayList) {
        this.mContext = context;
        this.codeArray = arrayList;
        this.callbacks = (MyAdapterCallBacks) context;
    }



    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.row_code, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.rb_select.setChecked(((CodeModelClass) this.codeArray.get(i)).getSelected());
        StringBuilder stringBuilder = new StringBuilder(((CodeModelClass) this.codeArray.get(i)).getCode());
        stringBuilder.insert(4, ' ');
        stringBuilder.insert(9, ' ');
        stringBuilder.insert(14, ' ');
        myViewHolder.tv_code.setText(stringBuilder);
        myViewHolder.tv_code.setSelected(true);
        myViewHolder.iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",myViewHolder.tv_code.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });
        myViewHolder.rb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.itemSelected(codeArray.get(myViewHolder.getAdapterPosition()));
            }
        });
    }

    public int getItemCount() {
        return this.codeArray.size();
    }
}

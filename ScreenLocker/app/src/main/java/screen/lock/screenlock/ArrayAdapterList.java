package screen.lock.screenlock;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class ArrayAdapterList extends RecyclerView.Adapter<ArrayAdapterList.ViewHolder> {
    ArrayList<Integer> imagespaths=new ArrayList<>();
    Context context;
    public OnItemClickListner listner;
    int selected;

    public ArrayAdapterList(Context context,ArrayList<Integer> imagespaths,int selected) {
        this.context=context;
        this.selected=selected;
        this.imagespaths = imagespaths;
    }

    public  interface OnItemClickListner{
         void onItemClick(View v,int pos);
    }
    public  void setOnItemClickListener(OnItemClickListner onItemClickListener){
        this.listner=onItemClickListener;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageviewholder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArrayAdapterList.ViewHolder holder, int position) {
        ImageView imageButton;
        RelativeLayout relativeLayout;
        imageButton=holder.imageButton;
        relativeLayout=holder.selected;
        if(imagespaths.get(position)==selected){
            relativeLayout.setVisibility(View.VISIBLE);
        }
            Glide.with(context)
                    .load(imagespaths.get(position))
                    .centerCrop()
                    .into(imageButton);
    }

    @Override
    public int getItemCount() {
        Log.d("thisinfo", imagespaths.size()+"//");
        return imagespaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageButton;
        RelativeLayout selected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton=(ImageView) itemView.findViewById(R.id.imageslist) ;
            selected=(RelativeLayout)itemView.findViewById(R.id.selectedrel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n=getAdapterPosition();
                    if(listner!=null && n!=-1){
                        listner.onItemClick(v,n);
                    }
                }
            });
        }
    }
}

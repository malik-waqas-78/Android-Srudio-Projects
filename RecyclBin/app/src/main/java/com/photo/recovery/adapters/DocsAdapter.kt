package com.photo.recovery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.photo.recovery.R
import com.photo.recovery.activites.PlayAudioAAT
import com.photo.recovery.activites.PlayVideoAAT
import com.photo.recovery.activites.ShowImageAAT
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.RowRecentlyDeletedAatBinding
import com.photo.recovery.models.AllFilesModelClassAAT
import java.io.File

class DocsAdapter(var context: Context, mfiles: ArrayList<AllFilesModelClassAAT>) :
    RecyclerView.Adapter<DocsAdapter.ViewHolder>()  {
    companion object{
        var images=ArrayList<AllFilesModelClassAAT>()
    }


    var files=ArrayList<AllFilesModelClassAAT>()
        set(value) {
            field=value
            images=value
        }

    init {
        files=mfiles
    }

    var selectedList = ArrayList<AllFilesModelClassAAT>()
    var selectionCallBack=context as SelectionCallBackAAT
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RowRecentlyDeletedAatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        with(holder) {
            val item=files[position]

            binding.tvDate.text=item.dateInFormat
            binding.tvFileName.text=item.fileName
            Glide.with(context).load(item.icon).into(binding.imageView2)
            item.color?.let { binding.view.setBackgroundColor(it) }
            binding.cbIsSeledted.setOnCheckedChangeListener(null)
            binding.cbIsSeledted.isChecked = selectedList.contains(item)
            binding.cbIsSeledted.setOnCheckedChangeListener { buttonView, isChecked ->
                try{
                    if(selectedList.contains(item)){
                        selectedList.remove(item)
                    }else{
                        selectedList.add(item)
                    }
                }catch (e: Exception){

                }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }



//
//            if(item.subType== MyConstantsAAT.IMAGE_FILE_TYPE||item.subType== MyConstantsAAT.VIDEO_FILE_TYPE){
//                Glide.with(context).load(item.filePath).into(binding.ivIcon)
//                binding.ivIcon.scaleType= ImageView.ScaleType.CENTER_CROP
//                binding.ivIcon.setPadding(0,0,0,0)
//                /*if(item.subType==MyConstants.IMAGE_FILE_TYPE){
//                    binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.purple))
//                }else{
//                    binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.red))
//                }*/
//            }else if(item.subType== MyConstantsAAT.AUDIO_FILE_TYPE){
//                binding.ivIcon.scaleType= ImageView.ScaleType.CENTER_INSIDE
//                //  binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.audio_color))
//            }else if(item.subType== MyConstantsAAT.PDF_FILE_TYPE){
//                binding.ivIcon.scaleType= ImageView.ScaleType.FIT_CENTER
//                Glide.with(context).load(R.drawable.ic_pdf).into(binding.ivIcon)
//                binding.ivIcon.setPadding(15,15,15,15)
//                //  binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.pdf_red))
//            }else if(item.subType== MyConstantsAAT.WORD_FILE_TYPE){
//                binding.ivIcon.scaleType= ImageView.ScaleType.FIT_CENTER
//                Glide.with(context).load(R.drawable.ic_word).into(binding.ivIcon)
//                binding.ivIcon.setPadding(15,15,15,15)
//
//                //   binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.word_blue))
//            }else if(item.subType== MyConstantsAAT.OTHER_FILE_TYPE){
//                binding.ivIcon.scaleType= ImageView.ScaleType.FIT_CENTER
//                Glide.with(context).load(R.drawable.ic_other).into(binding.ivIcon)
//                binding.ivIcon.setPadding(15,15,15,15)
//                //   binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.other_sky_blue))
//            }
//            binding.tvFileName.text=item.fileName
//            binding.tvDate.text=item.dateInFormat
//
//            binding.cbIsSelected.setOnCheckedChangeListener(null)
//            binding.cbIsSelected.isChecked = selectedList.contains(files[position])
//            binding.cbIsSelected.setOnCheckedChangeListener { buttonView, isChecked ->
//                if(selectedList.contains(files[position])){
//                    selectedList.remove(files[position])
//                }else{
//                    selectedList.add(files[position])
//                }
//                selectionCallBack.selectionChanged(selectedList.size)
//                notifyItemChanged(position)
//            }
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    inner class ViewHolder(val binding: RowRecentlyDeletedAatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if(adapterPosition==RecyclerView.NO_POSITION){
                    return@setOnClickListener
                }

                when(files[adapterPosition].subType){
                    MyConstantsAAT.AUDIO_FILE_TYPE->{
                        val intent= Intent(context, PlayAudioAAT::class.java)
                        intent.putExtra("url",files[adapterPosition].filePath)
                        intent.putExtra("name",files[adapterPosition].fileName)
                        context.startActivity(intent)
                    }
                    MyConstantsAAT.IMAGE_FILE_TYPE->{
                        ShowImageAAT.position=adapterPosition
                        val intent= Intent(context, ShowImageAAT::class.java)
                        context.startActivity(intent)
                    }
                    MyConstantsAAT.VIDEO_FILE_TYPE->{
                        val intent= Intent(context, PlayVideoAAT::class.java)
                        intent.putExtra("url",files[adapterPosition].filePath)
                        context.startActivity(intent)
                    }else->{
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    val uri = FileProvider.getUriForFile(
                        context.applicationContext,
                        context.applicationContext.resources.getString(R.string.authority),
                        File(files[adapterPosition].filePath)
                    )
                    intent.data = uri
                    context.startActivity(intent)
                }
                }
            }
        }
    }
}
package com.example.simplechat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplechat.databinding.ReceivedByBinding
import com.example.simplechat.databinding.ReceivedByImgBinding
import com.example.simplechat.databinding.SentByBinding
import com.example.simplechat.databinding.SentByImgBinding
import com.example.simplechat.modelclasses.Message

class ChatAdapter(val context: Context,val msgs:ArrayList<Message>):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        when(viewType) {
            Message.SENDER_TEXT->{
                return ChatViewHolder(
                    SentByBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            Message.SENDER_PIC->{
                return ChatViewHolder(
                        SentByImgBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
            }
            Message.RECEIVING_TEXT -> {

                return ChatViewHolder(
                    ReceivedByBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            Message.RECEVIING_PIC->{
                    return ChatViewHolder(
                        ReceivedByImgBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
            }
            else->{
                return ChatViewHolder(SentByBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }

        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        when(holder.itemViewType) {
            Message.SENDER_TEXT->{
                with(holder) {
                    val binding = _binding as SentByBinding
                    binding.textView2.text = msgs[position].txt
                }
            }
            Message.SENDER_PIC->{
                with(holder) {
                    val binding = _binding as SentByImgBinding
                    Glide.with(context).load(msgs[position].url).into(binding.imageView3)
                }
            }
            Message.RECEIVING_TEXT -> {

                with(holder){
                    val binding=_binding as ReceivedByBinding
                    binding.textView2.text=msgs[position].txt
                }
            }
            Message.RECEVIING_PIC->{
                with(holder) {
                    val binding = _binding as ReceivedByImgBinding
                    Glide.with(context).load(msgs[position].url).into(binding.imageView2)
                }
            }
            else->{
                with(holder) {
                    val binding = _binding as SentByBinding
                    binding.textView2.text = msgs[position].txt
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return msgs.size
    }

    override fun getItemViewType(position: Int): Int {
        return msgs[position].viewType
    }

    class ChatViewHolder(view: View):RecyclerView.ViewHolder(view) {
        lateinit var _binding:Any

        constructor(binding:SentByBinding) : this(binding.root){
            _binding = binding
        }
        constructor(binding:ReceivedByBinding) : this(binding.root){
            _binding = binding
        }
        constructor(binding:ReceivedByImgBinding) : this(binding.root){
            _binding = binding
        }
        constructor(binding:SentByImgBinding) : this(binding.root){
            _binding = binding
        }

    }

}
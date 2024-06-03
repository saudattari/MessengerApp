package com.example.messengerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.databinding.RecievingChatBinding
import com.example.messengerapp.databinding.SendingChatBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var messageList: ArrayList<Message>, var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_RECEIVE = 1
        const val ITEM_SENT = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_RECEIVE) {
            val binding = RecievingChatBinding.inflate(LayoutInflater.from(context), parent, false)
            recieveMessageViewHolder(binding)
        } else {
            val binding = SendingChatBinding.inflate(LayoutInflater.from(context), parent, false)
            sentMessageViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }
        override fun getItemCount(): Int {
            return messageList.size
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is sentMessageViewHolder) {
            holder.binding.sendingText.text = messageList[position].message
        } else if (holder is recieveMessageViewHolder) {
            holder.binding.recevingMessage.text = messageList[position].message
        }

    }


     class sentMessageViewHolder(var binding: SendingChatBinding) : RecyclerView.ViewHolder(binding.root)
     class recieveMessageViewHolder(var binding: RecievingChatBinding) : RecyclerView.ViewHolder(binding.root)
}
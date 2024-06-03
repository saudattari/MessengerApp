package com.example.messengerapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.databinding.UserLayoutBinding

class UserAdapter(val userList: ArrayList<User>, val context: Context):RecyclerView.Adapter<UserAdapter.myViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.binding.name.text = userList[position].uName
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", userList[position].uName)
            intent.putExtra("uid", userList[position].uid)

            context.startActivity(intent)
        }
    }
    inner class myViewHolder(var binding: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
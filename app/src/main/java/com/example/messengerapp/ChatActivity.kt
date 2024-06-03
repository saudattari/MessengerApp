package com.example.messengerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var list: ArrayList<Message>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var receiverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        list = ArrayList()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(list, this)
        recyclerView.adapter = adapter

        val senderId = auth.currentUser!!.uid
        val receiverId = intent.getStringExtra("uid")
            senderRoom = receiverId + senderId
            receiverRoom = senderId + receiverId


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //logic for adding data to recyclerview
        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        if (message != null)
                            list.add(message)

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        binding.imageView2.setOnClickListener {
            val messageText = binding.editTextText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(messageText, senderId)
                databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(message)
                    .addOnSuccessListener {
                        databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(message)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                                    binding.editTextText.setText("")
                                } else {
                                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                    Log.e("ChatActivity", "Failed to write message to receiverRoom: ${it.exception}")
                                }
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        Log.e("ChatActivity", "Failed to write message to senderRoom: ${it.message}")
                    }
            }
        }

    }
}
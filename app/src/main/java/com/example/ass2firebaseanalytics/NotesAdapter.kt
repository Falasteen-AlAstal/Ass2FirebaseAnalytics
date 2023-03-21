package com.example.ass2firebaseanalytics

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.notes_categories.view.*

class NotesAdapter (var activity: Activity, var data:ArrayList<NotesMoodle>): RecyclerView.Adapter<NotesAdapter.MyNotes>(){
    private lateinit var analytics: FirebaseAnalytics

    class MyNotes (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.name_categories
        val imageNotes = itemView.image_categories




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotes {

        val root = LayoutInflater.from(activity).inflate(R.layout.notes_categories,parent , false)
        analytics = Firebase.analytics
        return MyNotes(root)
    }

    override fun onBindViewHolder(holder: MyNotes, position: Int) {

        holder.name.text=data[position].name
        Glide.with(activity).load(data[position].image).into(holder.imageNotes)
        holder.itemView.setOnClickListener{
            select_content(data[position].id , data[position].name ,"item of Recycler View")
            val  i = Intent(activity , Details::class.java)
            i.putExtra("NoteName", data[position].name)
            i.putExtra("NoteDescription", data[position].description)
            i.putExtra("NoteCharacterCount", data[position].character_count)
            i.putExtra("NoteImage", data[position].image)

            activity.startActivity(i)
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun select_content( id:String , name: String , content_Type: String){

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, id)
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, content_Type)
        }

    }





}
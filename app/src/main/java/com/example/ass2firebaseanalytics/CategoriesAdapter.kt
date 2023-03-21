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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.notes_categories.view.*

class CategoriesAdapter (var activity: Activity, var data:ArrayList<CategoriesMoodle>): RecyclerView.Adapter<CategoriesAdapter.MyCategories>() {
    private lateinit var analytics: FirebaseAnalytics

    class MyCategories (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.name_categories
        val imageCategories = itemView.image_categories



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategories {

        val root = LayoutInflater.from(activity).inflate(R.layout.notes_categories,parent , false)
        analytics = Firebase.analytics
        return MyCategories(root)
    }

    override fun onBindViewHolder(holder: MyCategories, position: Int) {

        holder.name.text=data[position].name
        Glide.with(activity).load(data[position].image).into(holder.imageCategories)
        holder.itemView.setOnClickListener {
            select_content(data[position].id , data[position].name ,"item of Recycler View")
            val i = Intent(activity, Notes::class.java)
                i.putExtra("position" , position)
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
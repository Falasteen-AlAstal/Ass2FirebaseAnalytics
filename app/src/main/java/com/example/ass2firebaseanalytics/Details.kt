package com.example.ass2firebaseanalytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_details.*
import java.util.concurrent.TimeUnit

class Details : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var db: FirebaseFirestore
    private var timeSpent : Long = 0
    private var minutes: Long = 0
    private var seconds: Long= 0
    private var startTime : Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)


        startTime = System.currentTimeMillis()
        analytics = Firebase.analytics
        db = Firebase.firestore

       val name = intent.getStringExtra("NoteName")
       val description = intent.getStringExtra("NoteDescription")
       val characterCount = intent.getIntExtra("NoteCharacterCount" , 0)
       val noteImage = intent.getStringExtra("NoteImage")

        Note_Name.text = name
        Note_Description.text = description
        Note_Character_Count.text = characterCount.toString()
        Glide.with(this).load(noteImage).into(Note_image)

        screenTrack("Details" , "details")



    }


    fun screenTrack(screenClass:String , screenName: String){

        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){

            param(FirebaseAnalytics.Param.SCREEN_CLASS , screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME , screenName)

        }
    }



    fun addTime(userId: String , screenName: String , minutes : Long , seconds : Long  ){

        val data = hashMapOf(
            "screen_name" to screenName,
            "time_spent" to "$minutes m $seconds s",
            "userId" to userId
        )

        db.collection("times")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("Add Data", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Add Data", "Error adding document", e)
            }

    }

    override fun onPause() {
        super.onPause()
        val endTime = System.currentTimeMillis()
        timeSpent = endTime - startTime
        minutes = TimeUnit.MILLISECONDS.toMinutes(timeSpent)
        seconds = TimeUnit.MILLISECONDS.toSeconds(timeSpent) % 60
        addTime("Falasteen2002", "Details", minutes , seconds)
        Log.d("TAEM", "$minutes m $seconds s ")

    }


}
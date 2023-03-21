package com.example.ass2firebaseanalytics

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var notesArrayList: ArrayList<CategoriesMoodle>
    lateinit var progressDialog: ProgressDialog
    private var timeSpent : Long = 0
    private var minutes: Long = 0
    private var seconds: Long= 0
    private var startTime : Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTime = System.currentTimeMillis()

        db = Firebase.firestore
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading data")
        progressDialog.setCancelable(false)
        all_categories.layoutManager = LinearLayoutManager(this)
        notesArrayList = arrayListOf<CategoriesMoodle>()


        getAllCategories()

        screenTrack("MainActivity" , "home")



    }



    fun getAllCategories(){

        progressDialog.show()
        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                    val note= document.toObject(CategoriesMoodle::class.java)
                    notesArrayList.add(note)
                    Log.d("Read Data", "${document.id} => ${document.data}")


                }

                all_categories.adapter = CategoriesAdapter(this,notesArrayList)

            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }


    }


    fun screenTrack (screenClass:String , screenName: String){

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
        addTime("Falasteen2002", "Home", minutes , seconds)
        Log.d("TAEM", "$minutes m $seconds s ")

    }





}





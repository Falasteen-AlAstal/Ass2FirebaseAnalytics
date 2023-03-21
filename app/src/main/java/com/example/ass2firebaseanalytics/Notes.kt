package com.example.ass2firebaseanalytics

import android.app.ProgressDialog
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
import kotlinx.android.synthetic.main.activity_notes.*
import java.util.concurrent.TimeUnit

class Notes : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var notesArrayList: ArrayList<NotesMoodle>
    lateinit var progressDialog: ProgressDialog
    private var timeSpent : Long = 0
    private var minutes: Long = 0
    private var seconds: Long= 0
    private var startTime : Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        startTime = System.currentTimeMillis()
        db = Firebase.firestore
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading data")
        progressDialog.setCancelable(false)

        all_notes.layoutManager = LinearLayoutManager(this)
        notesArrayList = arrayListOf<NotesMoodle>()

        if (intent.getIntExtra("position" , 0) == 0){

            getAllNotes("1")
        }
        if (intent.getIntExtra("position" , 0) == 1){

            getAllNotes("2")
        }
        if (intent.getIntExtra("position" , 0) == 2){

            getAllNotes("3")
        }

        screenTrack("Notes" , "notes")



    }


    fun getAllNotes(id: String){

        progressDialog.show()
        db.collection("notes").whereEqualTo("categories_id" , id).get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                    val note= document.toObject(NotesMoodle::class.java)
                    notesArrayList.add(note)
                    Log.d("Read Data", "${document.id} => ${document.data}")


                }

                all_notes.adapter = NotesAdapter(this,notesArrayList)

            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }

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
        addTime("Falasteen2002", "Notes", minutes , seconds)
        Log.d("TAEM", "$minutes m $seconds s ")

    }






}
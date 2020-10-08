package com.kalela.innovexsupervisor.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.util.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragmentViewModel : ViewModel(), Observable {
    private val TAG = "MainActivityViewModel"

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    private var tasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = tasks

    private val taskCollectionRef = FirebaseFirestore.getInstance().collection("tasks")

    fun subscribeToRealtimeUpdates() {

        taskCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            val listOfTasks: ArrayList<Task> = ArrayList()

            firebaseFirestoreException?.let {
                statusMessage.value = Event("Error occurred while retrieving data.")
                return@addSnapshotListener
            }

            querySnapshot?.let {
                for (document in it) {
                    val task = document.toObject<Task>()
                    val sdf = SimpleDateFormat("MM/dd/yyyy")
                    val milliseconds = task.actual_time.seconds * 1000 + task.actual_time.nanoseconds / 1000000
                    task.actualTimeAsString = "at ${sdf.format(milliseconds).toString()}"

                    listOfTasks.add(task)
                }
            }

            if (listOfTasks.size > 0) {
                tasks.value = listOfTasks
            }
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
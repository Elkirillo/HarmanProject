package com.example.finalproject.fragments.add


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
import com.example.finalproject.model.User
import com.example.finalproject.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class addFragment : Fragment() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    public lateinit var  mUserViewModel : UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_btn.setOnClickListener{
            insertDataToDatabase()
        }

        return view
    }


    private fun insertDataToDatabase() {
        val place = addFirstName_et.text.toString()
        val myEvent = addLastName_et.text.toString()
        val  myTime = editTextNumber.text.toString()

        if(inputCheck(place, myEvent, myTime)){
            //создаем объект пользователя
            val user = User (0, place, myEvent, myTime)
            //добавляем его в базу данных
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Успешно добавлен!", Toast.LENGTH_SHORT).show()
            //создание и отправление уведомлений, будет реализовано позже, не работает
            //createNotificationChannel()
            //sendNotification(place, myEvent, myTime)
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

    }

 /*   private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText ="Notif"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendNotification (place: String, myEvent: String, myTime: String) {
        val intent = Intent (this, addFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, 0)



        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_add)
            .setContentTitle(myEvent)
            .setContentText(place + " время прибытия " + myTime)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }

    }
*/

    private fun inputCheck(place: String, myEvent: String, myTime : String): Boolean{
        return !(TextUtils.isEmpty(place) || TextUtils.isEmpty(myEvent) || TextUtils.isEmpty(myTime))
    }


}
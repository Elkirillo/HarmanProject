package com.example.finalproject.fragments.add


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.AlarmReceiver
import com.example.finalproject.R
import com.example.finalproject.model.User
import com.example.finalproject.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.util.*


class Fragment : Fragment() {

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
            val user = User(0, place, myEvent, myTime)
            //добавляем его в базу данных
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Успешно добавлен!", Toast.LENGTH_SHORT).show()
            //создание и отправление уведомлений
            createNotificationChannel()
            sendNotification(place, myEvent, myTime)
            // alarmNotif()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText ="Notif"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

    }


    private fun sendNotification(place: String, myEvent: String, myTime: String) {
        val intent = Intent(this.requireContext(), Fragment::class.java).apply {
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this.requireContext(), 0, intent, 0)

        var builder = NotificationCompat.Builder(this.requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(myEvent)
                .setContentIntent(pendingIntent)
                .setContentText("Место: " + place + " время прибытия: " + myTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this.requireContext())){
            notify(notificationId, builder.build())
        }

    }
    private fun alarmNotif () {
        val intent = Intent(this.requireContext(), AlarmReceiver::class.java)
        // get a Calendar object with current time
        val cal = Calendar.getInstance()
        // add 5 minutes to the calendar object
        cal.add(Calendar.SECOND, 10)
        intent.putExtra("alarm_message", "Нужно выходить!")
        //In reality, you would want to have a static variable for the request code instead of 192837
        val sender = PendingIntent.getBroadcast(this.requireContext(), 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Get the AlarmManager service
        val am = ContextCompat.getSystemService(this.requireContext(), ALARM_SERVICE::class.java) as AlarmManager?
        // am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender)
        am!!.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 5, sender)
    }

    private fun inputCheck(place: String, myEvent: String, myTime: String): Boolean{
        return !(TextUtils.isEmpty(place) || TextUtils.isEmpty(myEvent) || TextUtils.isEmpty(myTime))
    }


}
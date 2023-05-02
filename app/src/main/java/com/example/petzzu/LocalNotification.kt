package com.example.petzzu

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.petzzu.databinding.ActivityLocalNotificationBinding
import java.text.DateFormat
import java.util.*

class LocalNotification : AppCompatActivity() {

    private lateinit var binding: ActivityLocalNotificationBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLocalNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        binding.submitButton.setOnClickListener {
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
        val intent= Intent(applicationContext,Notifications::class.java)
        val title=binding.titleET.text.toString()
        val message=binding.messageET.text.toString()
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)

        val pendingIntent=PendingIntent.getBroadcast(applicationContext, notificationId,intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time=getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time,title,message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date= Date(time)
        val dataFormat=android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat=android.text.format.DateFormat.getTimeFormat(applicationContext)
        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage("Title: "+title+"\nMessage: "+message+"\nAt: "+dataFormat.format(date)+" "+timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(): Long {
        val minute=binding.timePicker.minute
        val hour=binding.timePicker.hour
        val day=binding.datePicker.dayOfMonth
        val month=binding.datePicker.month
        val year=binding.datePicker.year

        val calendar=Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name="Notification Channel"
        val desc="Description of the Channel"
        val importance=NotificationManager.IMPORTANCE_DEFAULT
        val channel=NotificationChannel(channelId,name,importance)
        channel.description=desc
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
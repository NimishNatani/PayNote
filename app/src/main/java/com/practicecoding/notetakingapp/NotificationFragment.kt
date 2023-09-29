package com.practicecoding.notetakingapp

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.navArgs
import com.practicecoding.notetakingapp.databinding.FragmentNotificationBinding
import com.practicecoding.notetakingapp.databinding.FragmentUpdateNoteBinding
import com.practicecoding.notetakingapp.model.Notification
import com.practicecoding.notetakingapp.model.channelID
import com.practicecoding.notetakingapp.model.messageExtra
import com.practicecoding.notetakingapp.model.notificationID
import com.practicecoding.notetakingapp.model.titleExtra
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide
import java.util.Calendar
import java.util.Date


class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private var _binding: FragmentNotificationBinding?=null
    private val binding get() = _binding!!
lateinit var currentnote: Note
lateinit var hidecurrentnote: NoteHide
    private val args:NotificationFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater,container,false)
        return binding.root    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.notification != null){
        currentnote = args.notification!!
        binding.titleedt.setText("${currentnote.noteTitle}")
        binding.disedt.setText("${currentnote.noteBody}")}
        else {
            hidecurrentnote = args.hidenotification!!
            binding.titleedt.setText("${hidecurrentnote.noteTitle}")
            binding.disedt.setText("${hidecurrentnote.noteBody}")
        }

        createnotificationchannel()
        binding.button.setOnClickListener(){
            schedulenotification()
        }
    }

    private fun schedulenotification() {
        val intent = Intent(context,Notification::class.java)
        val title = binding.titleedt.text.toString()
        val message = binding.disedt.text.toString()
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)
        val pendingIntent = PendingIntent.getBroadcast(
            context, notificationID,intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmmanager  = getActivity()?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmmanager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP
            ,time,pendingIntent
        )
        showAlert(time,title,message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
val date = Date(time)
        val dateformat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeformat = android.text.format.DateFormat.getTimeFormat(context)
        AlertDialog.Builder(context)
            .setTitle("Notification Schedule")
            .setMessage("Title: "+title+"\nMessage: "+message+ "\nAt: "+ dateformat.format(date)
            + " " + timeformat.format(date))
            .setPositiveButton("Okay"){_,_->}
            .show()

    }


    private fun getTime(): Long {
val minute = binding.timepicker.minute
        val hour = binding.timepicker.hour
        val day = binding.datepicker.dayOfMonth
        val month = binding.datepicker.month
        val year = binding.datepicker.year

        val calender  = Calendar.getInstance()
        calender.set(year,month,day,hour,minute)
        return calender.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createnotificationchannel() {
val name = "Notify Channel"
    val desc = "A Description of the channel"
    val importance= NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelID,name,importance)
    channel.description = desc
    val notificationManager = getActivity()?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}
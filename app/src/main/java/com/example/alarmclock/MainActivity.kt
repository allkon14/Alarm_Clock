package com.example.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var con: Context
    lateinit var btnSet: Button
    lateinit var btnStop: Button
    var hour:Int = 0
    var min:Int = 0
    lateinit var pi: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.con = this
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = findViewById(R.id.time) as TimePicker
        btnStop = findViewById(R.id.stop_alarm) as Button
        btnSet = findViewById(R.id.set_alarm) as Button
        update_text = findViewById(R.id.up_text) as TextView

        var myIntent = Intent(this, AlarmReceiver::class.java)

        tp.setIs24HourView(true)

        var calendar: Calendar = Calendar.getInstance()

        btnSet.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.i("Inform","aht")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    calendar.set(Calendar.HOUR_OF_DAY, tp.hour)
                    calendar.set(Calendar.MINUTE, tp.minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = tp.hour
                    min = tp.minute
                    Log.i("Inform","if")
                }
                else
                {
                    calendar.set(Calendar.HOUR_OF_DAY, tp.currentHour)
                    calendar.set(Calendar.MINUTE, tp.currentMinute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = tp.currentHour
                    min = tp.currentMinute
                    Log.i("Inform","else")
                }
                val hr_str: String = hour.toString()
                var min_str: String = min.toString()

                if(min<10)
                    min_str = "0$min"
                Log.i("Inform","after")

                Log.i("Inform","before text")
                set_alarm_text("Alarm set to: $hr_str : $min_str")
                Log.i("Inform","after text")
                myIntent.putExtra("extra", "on")
                Log.i("Inform", "before PendingIntent")
                pi = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                Log.i("Inform", "after PendingIntent")
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        })

        btnStop.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.i("Inform","stop button")
                set_alarm_text("Alarm off")
                myIntent.putExtra("extra", "off")

                pi = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                am.cancel(pi)
                sendBroadcast(myIntent)
            }

        })

    }

    private fun set_alarm_text(s: String) {
        Log.i("setText","success")
        //val text = "Пора покормить кота!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
        //update_text.setText(s)

    }
}
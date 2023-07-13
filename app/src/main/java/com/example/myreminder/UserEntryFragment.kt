package com.example.myreminder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myreminder.databinding.FragmentUserEntryBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class UserEntryFragment : Fragment() {
    lateinit var binding: FragmentUserEntryBinding
    var fromedt: Boolean = false
    var selectedValue: String = ""
    var ID: Int = 0
    var activity=MainActivity()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_entry, container, false)

        inflateviews()
        setViewEvents()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inflateviews() {
        val days = ArrayList<String>()
        val mnth = arrayOf("January", "February", "March", "April", "May", "June", "July", "Augest", "September", "October", "November", "December")
        val hour = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val am_pm = arrayOf("AM","PM")
        val min = ArrayList<String>()
        for (i in (1..31)) {
            days.add(i.toString())
        }
        for (i in 0..60) {
            min.add(i.toString())
        }

        val DAYS = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
        DAYS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val MNTH = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mnth)
        MNTH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val HOUR = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hour)
        HOUR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val MIN = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, min)
        MIN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val AM_PM = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, am_pm)
        AM_PM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.enterday.adapter = DAYS
        binding.entermounth.adapter = MNTH
        binding.hour.adapter = HOUR
        binding.min.adapter = MIN
        binding.amPm.adapter = AM_PM



        val args = arguments
        if (args?.isEmpty != true && args?.getBoolean("fromcus") == false ) {
                ID = args?.getInt("id")!!
                fromedt = args?.getBoolean("fromedt") == true
                val name = args?.getString("name")
                val discription = args?.getString("discription")
                val mnth = args?.getString("mnth")
                val days = args?.getString("days")
                val hour = args?.getString("hour")
                val min = args?.getString("min")
                val ampm = args?.getString("ampm")
                binding.enteredname.setText(name)
                binding.Discription.setText(discription)
                binding.entermounth.setSelection(MNTH.getPosition(mnth))
                binding.enterday.setSelection(DAYS.getPosition(days))
                binding.hour.setSelection(HOUR.getPosition(hour))
                binding.min.setSelection(MIN.getPosition(min))
                binding.amPm.setSelection(AM_PM.getPosition(ampm))
        }else{
            binding.entermounth.setSelection(MNTH.getPosition(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())))
            binding.enterday.setSelection(DAYS.getPosition(SimpleDateFormat("d").format(Date())))
            if(Calendar.getInstance().get(Calendar.HOUR).toString()=="0"){
                binding.hour.setSelection(HOUR.getPosition("12"))
            }else{
                binding.hour.setSelection(HOUR.getPosition(Calendar.getInstance().get(Calendar.HOUR).toString()))
            }
            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>12){
                binding.amPm.setSelection(AM_PM.getPosition("PM"))
            }else
                binding.amPm.setSelection(AM_PM.getPosition("AM"))
            binding.min.setSelection(MIN.getPosition(Calendar.getInstance().get(Calendar.MINUTE).toString()))

        }
        if (args?.getBoolean("addFromCustom",false) == true||args?.getBoolean("fromcus",false) == true) {
            binding.dailyreminder.visibility=View.VISIBLE
            binding.eventreminder.visibility=View.GONE
            customreminder(hour,min,am_pm)
        }else{
            binding.dailyreminder.visibility=View.GONE
            binding.eventreminder.visibility=View.VISIBLE
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun customreminder(
        hour: Array<String>,
        min: ArrayList<String>,
        am_pm: Array<String>
    ) {
        val weekdays= arrayOf("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY","MONDAY-FRIDAY","DAILY")
        val WEEKDAYS = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weekdays)
        WEEKDAYS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val HOUR = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hour)
        HOUR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val MIN = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, min)
        MIN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val AM_PM = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, am_pm)
        AM_PM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.weekdays.adapter=WEEKDAYS
        binding.dHour.adapter = HOUR
        binding.dMin.adapter = MIN
        binding.dAmPm.adapter = AM_PM

        val args = arguments
        if (args?.getBoolean("fromcus") == true) {
            ID = args?.getInt("id")!!
            fromedt = args?.getBoolean("fromedt") == true
            val name = args?.getString("name")
            val discription = args?.getString("discription")
            val hour = args?.getString("hour")
            val min = args?.getString("min")
            val ampm = args?.getString("ampm")
            val weekdays = args?.getString("weekdays")
            binding.dEnteredname.setText(name)
            binding.dDiscription.setText(discription)
            binding.dHour.setSelection(HOUR.getPosition(hour))
            binding.dMin.setSelection(MIN.getPosition(min))
            binding.dAmPm.setSelection(AM_PM.getPosition(ampm))
            binding.weekdays.setSelection(WEEKDAYS.getPosition(weekdays))
        } else {
            if(Calendar.getInstance().get(Calendar.HOUR).toString()=="0"){
                binding.dHour.setSelection(HOUR.getPosition("12"))
            }else{
                binding.dHour.setSelection(HOUR.getPosition(Calendar.getInstance().get(Calendar.HOUR).toString()))
            }
            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>12){
                binding.dAmPm.setSelection(AM_PM.getPosition("PM"))
            }else
                binding.dAmPm.setSelection(AM_PM.getPosition("AM"))
            binding.dMin.setSelection(MIN.getPosition(Calendar.getInstance().get(Calendar.MINUTE).toString()))
            binding.weekdays.setSelection(WEEKDAYS.getPosition(LocalDate.now().dayOfWeek.toString()))
        }




    }


    private fun setViewEvents() {
        binding.save.setOnClickListener {
            val db = MyDatabaseHelper(requireContext())
            var name = binding.enteredname.text.toString()
            var discription = binding.Discription.text.toString()
            var mnth = binding.entermounth.selectedItem as String
            var days = binding.enterday.selectedItem as String
            var hour = binding.hour.selectedItem as String
            var min = binding.min.selectedItem as String
            var ampm = binding.amPm.selectedItem as String
            if (fromedt) {
                db.updateValues(ID,name, discription, mnth, days, hour, min,ampm)
            } else {
                db.insertData(name, discription, mnth, days, hour, min,ampm)
            }
            arguments?.putBoolean("saveFromCustom", false)
            val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
            findNavController().navigate(R.id.userMasterFragment, arguments, navOptions)
        }
        binding.dSave.setOnClickListener {
            val db = MyDatabaseHelper(requireContext())
            var name = binding.dEnteredname.text.toString()
            var discription = binding.dDiscription.text.toString()
            var hour = binding.dHour.selectedItem as String
            var min = binding.dMin.selectedItem as String
            var ampm = binding.dAmPm.selectedItem as String
            var weekdays = binding.weekdays.selectedItem as String
            if (arguments?.isEmpty != true && arguments?.getBoolean("fromcus") == true) {
                db.updateValues1(ID,name, discription, hour, min,ampm,weekdays)
            } else {
                db.insertData1(name, discription,hour, min,ampm,weekdays)
            }
            arguments?.putBoolean("saveFromCustom", true)
            val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
            findNavController().navigate(R.id.userMasterFragment, arguments, navOptions)

        }

    }

}
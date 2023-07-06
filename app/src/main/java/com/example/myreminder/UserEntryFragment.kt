package com.example.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myreminder.databinding.FragmentUserEntryBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UserEntryFragment : Fragment() {
    lateinit var binding: FragmentUserEntryBinding
    var fromedt: Boolean = false
    var selectedValue: String = ""
    var ID: Int = 0

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

    private fun inflateviews() {
        val days = ArrayList<String>()
        val mnth = arrayOf("January", "February", "March", "April", "May", "June", "July", "Augest", "September", "October", "November", "December")
        val hour = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val am_pm = arrayOf("AM","PM")
        val min = ArrayList<String>()
        for (i in (1..31)) {
            days.add(i.toString())
        }
        for (i in 1..60) {
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
        if (args?.isEmpty != true) {
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
                binding.hour.setSelection(DAYS.getPosition(Calendar.getInstance().get(Calendar.HOUR).toString()))
            }
            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>12){
                binding.amPm.setSelection(AM_PM.getPosition("PM"))
            }else
                binding.amPm.setSelection(AM_PM.getPosition("AM"))
            binding.min.setSelection(MIN.getPosition(Calendar.getInstance().get(Calendar.MINUTE).toString()))

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


            findNavController().navigate(R.id.action_userEntryFragment_to_userMasterFragment)
        }
    }
}
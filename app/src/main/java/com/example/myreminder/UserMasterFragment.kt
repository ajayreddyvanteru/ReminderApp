package com.example.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminder.databinding.FragmentUserMasterBinding


class UserMasterFragment : Fragment(), OnItemClickListener {
    lateinit var binding: FragmentUserMasterBinding
    private var eventlist: ArrayList<Item> = ArrayList()
    private var customlist: ArrayList<Item1> = ArrayList()
    var activity=MainActivity()
    private lateinit var eventadapter: ItemAdapter
    private lateinit var customWeekAdapter: customWeekAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_master, container, false)
        if(arguments?.getBoolean("saveFromCustom",false) == true){
            setViewEvents("custom")
            activity.fromcustom=true
        }else{
            setViewEvents("event")
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.setQuery("",false)
    }

    private fun setViewEvents(name: String) {
        var db = MyDatabaseHelper(requireContext())
        eventlist = db.getUsers()
        customlist = db.getUsers1()
        val recyclerView: RecyclerView = binding.recyclerView

        if(name.equals("event") && activity.fromcustom==false){
            binding.Title.setText("Event Reminder")
            binding.searchView.visibility=View.GONE
            binding.searchicon.visibility=View.VISIBLE
            binding.searchView.setQuery("", true);
            binding.Title.visibility=View.VISIBLE
            eventadapter = ItemAdapter(requireContext(), eventlist, this)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = eventadapter
        } else {
            customWeekAdapter = customWeekAdapter(requireContext(), customlist, this)
            binding.Title.setText("Custom Reminder")
            activity.fromcustom=true
            binding.searchView.setQuery("", false);
            binding.searchView.visibility=View.GONE
            binding.searchicon.visibility=View.VISIBLE
            binding.Title.visibility=View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = customWeekAdapter
        }


        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks here
            when (menuItem.itemId) {
                R.id.event -> {
                    activity.fromcustom=false
                    setViewEvents("event")

                }
                R.id.custom -> {
                    activity.fromcustom=true
                    setViewEvents("custom")
                }
            }

            // Close the drawer after handling the click
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            // Return true to indicate that the click event has been handled
            true
        }

        binding.menu.setOnClickListener(View.OnClickListener { binding.drawerLayout!!.openDrawer(GravityCompat.START) })
        binding.searchicon.setOnClickListener {
            binding.searchView.visibility=View.VISIBLE
            binding.searchicon.visibility=View.GONE
            binding.Title.visibility=View.GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!activity.fromcustom) {
                        eventadapter.filter(newText.orEmpty())
                    } else {
                        customWeekAdapter.filter(newText.orEmpty())
                    }
                    return true
                }
            })



        binding.buttonadd.setOnClickListener {
            val args = Bundle()
            if (activity.fromcustom) {
                args.putBoolean("addFromCustom", true)
                val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
                findNavController().navigate(R.id.userEntryFragment, args, navOptions)
            }else{
                findNavController().navigate(R.id.action_userMasterFragment_to_userEntryFragment)
            }
        }

    }

    override fun onItemClick(position: Int) {
        if (!activity.fromcustom) {
            val item = eventadapter.items[position]
            /*val data = "Hello, Destination!"
            val adata = "Hello, Destination!"
            val action = UserMasterFragmentDirections.actionUserMasterFragmentToUserEntryFragment(data,adata) -----------for this need to creats args in nav graph
            findNavController().navigate(action)*/
            val id = item.id
            val name = item.name
            val discription = item.Discription
            val mnth = item.mnth
            val days = item.days
            val hour = item.hour
            val min = item.min
            val ampm = item.ampm

            val args = Bundle()
            args.putInt("id", id)
            args.putString("name", name)
            args.putBoolean("fromedt", true)
            args.putString("discription", discription)
            args.putString("mnth", mnth)
            args.putString("days", days)
            args.putString("hour", hour)
            args.putString("min", min)
            args.putString("ampm", ampm)
            args.putString("ampm", ampm)

            val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
            findNavController().navigate(R.id.userEntryFragment, args, navOptions)
        } else {
            val item = customWeekAdapter.items[position]
            /*val data = "Hello, Destination!"
            val adata = "Hello, Destination!"
            val action = UserMasterFragmentDirections.actionUserMasterFragmentToUserEntryFragment(data,adata) -----------for this need to creats args in nav graph
            findNavController().navigate(action)*/
            val id = item.id
            val name = item.name
            val discription = item.Discription
            val hour = item.hour
            val min = item.min
            val ampm = item.ampm
            val weekdays=item.weekdays

            val args = Bundle()
            args.putInt("id", id)
            args.putString("name", name)
            args.putString("discription", discription)
            args.putString("hour", hour)
            args.putString("min", min)
            args.putString("ampm", ampm)
            args.putString("weekdays", weekdays)
            args.putBoolean("fromcus", activity.fromcustom)
            val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
            findNavController().navigate(R.id.userEntryFragment, args, navOptions)
        }


    }


}
package com.example.myreminder

import android.R.attr.button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminder.databinding.FragmentUserMasterBinding
import com.google.android.material.navigation.NavigationView


class UserMasterFragment : Fragment(), OnItemClickListener {
    lateinit var binding: FragmentUserMasterBinding
    private var mlist: ArrayList<Item> = ArrayList()
    private lateinit var adapter1: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_master, container, false)
        setViewEvents()
        return binding.root
    }


    private fun setViewEvents() {
        var db = MyDatabaseHelper(requireContext())
        mlist = db.getUsers()
        adapter1 = ItemAdapter(requireContext(), mlist, this)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter1

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks here
            when (menuItem.itemId) {
                R.id.nav_account -> {
                    // Handle menu item 1 click
                    Toast.makeText(requireContext(), "message", Toast.LENGTH_SHORT).show()
                    // Example: display a toast message
                }
                R.id.nav_settings -> {
                    // Handle menu item 2 click
                    // Example: display a toast message
                    Toast.makeText(requireContext(), "message2", Toast.LENGTH_SHORT).show()

                }
                R.id.nav_logout -> {
                    // Handle menu item 3 click
                    // Example: display a toast message
                    Toast.makeText(requireContext(), "message3", Toast.LENGTH_SHORT).show()

                }
            }

            // Close the drawer after handling the click
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            // Return true to indicate that the click event has been handled
            true
        }

        binding.menu.setOnClickListener(View.OnClickListener { binding.drawerLayout!!.openDrawer(GravityCompat.START) })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter1.filter(newText.orEmpty())
                return true
            }
        })

        binding.buttonadd.setOnClickListener {
            findNavController().navigate(R.id.action_userMasterFragment_to_userEntryFragment)
        }

    }

    override fun onItemClick(position: Int) {
        val item = adapter1.items[position]
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

//        val destinationFragment = UserEntryFragment()
//        destinationFragment.arguments = args

//        // or

        val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
        findNavController().navigate(R.id.userEntryFragment, args, navOptions)

    }


}
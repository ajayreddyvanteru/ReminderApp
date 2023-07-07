package com.example.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminder.databinding.FragmentUserMasterBinding
import java.util.*


class UserMasterFragment : Fragment(), OnItemClickListener {
    lateinit var binding: FragmentUserMasterBinding
    private var mlist: ArrayList<Item> = ArrayList()
    private lateinit var adapter: ItemAdapter

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

        adapter = ItemAdapter(requireContext(), mlist, this)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })

        binding.buttonadd.setOnClickListener {
            findNavController().navigate(R.id.action_userMasterFragment_to_userEntryFragment)
        }

    }

    override fun onItemClick(position: Int) {
        val item = adapter.items[position]
//        val data = "Hello, Destination!"
//        val adata = "Hello, Destination!"
//        val action = UserMasterFragmentDirections.actionUserMasterFragmentToUserEntryFragment(data,adata) -----------for this need to creats args in nav graph
//        findNavController().navigate(action)
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
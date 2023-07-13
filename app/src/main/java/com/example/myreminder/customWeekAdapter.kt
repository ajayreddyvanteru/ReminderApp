package com.example.myreminder
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView



class  customWeekAdapter(private val context: Context, var items: ArrayList<Item1>, private val listener: OnItemClickListener) : RecyclerView.Adapter<customWeekAdapter.MyViewHolder>() {

    private var filteredItemList: List<Item1>? = null
    private var temp: ArrayList<Item1> =items

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.weekdays_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }


    private fun showCustomDialog(holder: MyViewHolder, position: Int, name: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(dialogView)


        val okButton = dialogView.findViewById<Button>(R.id.button_ok)
        val cancelButton = dialogView.findViewById<Button>(R.id.button_cancel)
        val title=dialogView.findViewById<TextView>(R.id.text_title)
        title.setText("Are you sure to delete an event of "+name+"?")

        var alertDialog = alertDialogBuilder.create()

        okButton.setOnClickListener {
            val db= MyDatabaseHelper(context)
            db.deleteRow1(holder.id)
            items.removeAt(position)
            notifyDataSetChanged()
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun filter(query: String) {
        items=temp as ArrayList<Item1>
        filteredItemList = if (query.isEmpty()) {
            items
        } else {
            items.filter { Item1 ->
                Item1.name.toLowerCase().contains(query.toLowerCase())
            }
        }
        items= filteredItemList as ArrayList<Item1>
        notifyDataSetChanged()

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val Item1 = items[position]
        filteredItemList = ArrayList(items)
        holder.name.text =Item1.name
        holder.id=Item1.id
        holder.Discription.text =Item1.Discription
        holder.hour.text = Item1.hour
        holder.min.text = Item1.min
        holder.weekday.text = Item1.weekdays
        holder.delete.setOnClickListener {
            showCustomDialog(holder,position,Item1.name)
        }
        holder.itemView.setOnClickListener {
            // Trigger the Item1 click listener
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(itemView1: View) : RecyclerView.ViewHolder(itemView1) {
        var name: TextView
        var id: Int = 0
        var Discription: TextView
        var hour: TextView
        var min: TextView
        val delete: ImageView
        val weekday: TextView

        init {
            name = itemView.findViewById(R.id.name)
            Discription = itemView.findViewById(R.id.Discription)
            hour = itemView.findViewById(R.id.hour)
            min = itemView.findViewById(R.id.min)
            delete=itemView.findViewById(R.id.delete)
            weekday=itemView.findViewById(R.id.weekday)
            itemView1.setOnTouchListener { v, event -> false }
        }
    }
}

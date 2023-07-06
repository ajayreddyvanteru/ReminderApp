package com.example.myreminder
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.text.toUpperCase
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class ItemAdapter(private val context: Context, val items: ArrayList<Item>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
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
            val db=MyDatabaseHelper(context)
            db.deleteRow(holder.id)
            items.removeAt(position)
            notifyDataSetChanged()
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.name.text =item.name
        holder.id=item.id
        holder.Discription.text =item.Discription
        holder.mnth.text =item.mnth
        holder.day.text = item.days
        holder.hour.text = item.hour
        holder.min.text = item.min
        holder.delete.setOnClickListener {
            showCustomDialog(holder,position,item.name)
        }
        holder.itemView.setOnClickListener {
            // Trigger the item click listener
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var id: Int = 0
        var Discription: TextView
        var mnth: TextView
        var day: TextView
        var hour: TextView
        var min: TextView
        val delete: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            Discription = itemView.findViewById(R.id.Discription)
            mnth = itemView.findViewById(R.id.mnth)
            day = itemView.findViewById(R.id.day)
            hour = itemView.findViewById(R.id.hour)
            min = itemView.findViewById(R.id.min)
            delete=itemView.findViewById(R.id.delete)
            itemView.setOnTouchListener { v, event -> false }
        }
    }
}

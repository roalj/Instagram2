package com.omejc.instagram2

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject


import com.omejc.instagram2.ImageFragment.OnListFragmentInteractionListener
import com.omejc.instagram2.models.Image
import com.omejc.instagram2.requests.CatalogRequest
import com.omejc.instagram2.requests.FilterRequest
import com.omejc.instagram2.requests.ImageUploadRequest

import kotlinx.android.synthetic.main.fragment_image.view.*


class MyImageRecyclerViewAdapter(
    private val mValues: ArrayList<Image>,
    private val mListener: OnListFragmentInteractionListener?,
    private val context: Context


) : RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    private val queue = Volley.newRequestQueue(context)

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Image
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            //mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.title.text = item.title

        mHandler = Handler()



        val getJsonLoad  = ImageUploadRequest.getJsonLoadRequest(item.mongoId, holder.image)
        ImageUploadRequest.makeRequest(getJsonLoad, context)

        holder.image.contentDescription = item.desciption
        holder.deleteBtn.setOnClickListener {
            CatalogRequest.makeRequestDeleteImage(context, item.id.toString())
            mValues.remove(item)
            notifyDataSetChanged()
            Log.d("delete ", "delete click")
        }
        holder.rainbowBtn.setOnClickListener {
            if (!holder.isRunning) {
                holder.isRunning = true
                holder.mRunnable = Runnable {
                    // Do something here
                    Log.d("ADAPTER", "call Filter")
                    val data = context.resources.getStringArray(R.array.filter_list)
                    val rnds = (0..3).random()
                    callFilterRequest(item.mongoId, data.get(rnds), holder.image)

                    // Schedule the task to repeat after 1 second
                    holder.handler.postDelayed(
                        holder.mRunnable, // Runnable
                        200 // Delay in milliseconds
                    )
                }

                // Schedule the task to repeat after 1 second
                holder.handler.postDelayed(
                    holder.mRunnable, // Runnable
                    1000 // Delay in milliseconds
                )
                Log.d("delete ", "delete click")
            }else{
                holder.isRunning = false
                holder.handler.removeCallbacks(holder.mRunnable)
            }
        }

        holder.spinner.adapter = getAdapter()
        holder.spinner.setSelection(Adapter.NO_SELECTION, true)
        holder.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int,
                                        p3: Long) {
                val spinnerFilter = holder.spinner.selectedItem.toString()
                val imageData  = FilterRequest.getMongoIdJsonObject(item.mongoId, spinnerFilter)
                val getFilterRequest = FilterRequest.getJsonFilterRequest(imageData, holder.image)
                ImageUploadRequest.makeRequest(getFilterRequest, context)

                println("adapter $p2$spinnerFilter")

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    private fun callFilterRequest(mongoId:String, filter:String, image:ImageView){
        val imageData  = FilterRequest.getMongoIdJsonObject(mongoId, filter)
        val getFilterRequest = FilterRequest.getJsonFilterRequest(imageData, image)
        queue.add(getFilterRequest)
    }

    private fun getAdapter():SpinnerAdapter{
        val adapter = ArrayAdapter.createFromResource(context, R.array.filter_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.item_number
        val image: ImageView = mView.image
        val spinner: Spinner = mView.spinner2
        val deleteBtn: ImageButton = mView.deleteBtn
        val rainbowBtn: ImageButton = mView.rainbowBtn
        val handler: Handler = Handler()
        var mRunnable:Runnable = Runnable{}
        var isRunning:Boolean = false

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}

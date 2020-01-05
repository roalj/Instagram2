package com.omejc.instagram2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.GsonBuilder
import com.omejc.instagram2.dummy.DummyContent
import com.omejc.instagram2.dummy.DummyContent.DummyItem
import org.json.JSONArray
import org.json.JSONException

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ImageFragment.OnListFragmentInteractionListener] interface.
 */
class ImageFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var TAG = "ImageFragment"
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { columnCount = it.getInt(ARG_COLUMN_COUNT) }

        RequestHelper.makeRequest(context, myImageListener)

    }


    private var myImageListener: Response.Listener<JSONArray> =
        Response.Listener { response ->
            val gson = GsonBuilder().create()
            val images = gson.fromJson(response.toString(), Array<Image>::class.java).toList()
            Log.d(TAG, "image length:$images")
            for (name:Image in images){
                Log.d(TAG, "!!!name${name.title}")
                Log.d(TAG, name.desciption)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyImageRecyclerViewAdapter(DummyContent.ITEMS, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

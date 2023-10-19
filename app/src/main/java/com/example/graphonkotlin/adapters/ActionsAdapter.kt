package com.example.graphonkotlin.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.graphonkotlin.R
import com.example.graphonkotlin.services.FIleService
import com.example.graphonkotlin.utils.States
import com.example.graphonkotlin.views.BlankFragment
import com.example.graphonkotlin.views.DrawView
import java.lang.String
import kotlin.Int
import kotlin.arrayOf

class ActionsAdapter(
    context: Context,
    var imageViews: MutableList<Drawable?>
) : RecyclerView.Adapter<ActionsAdapter.FunctionsAdapterHolder?>() {
    interface OnStateClickListener {
        fun onStateClick(position: Int)
    }

    private val inflater: LayoutInflater
    var fIleService: FIleService
    private val context: Context

    init {
        inflater = LayoutInflater.from(context)
        fIleService = FIleService(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionsAdapterHolder {
        val view: View = inflater.inflate(R.layout.available_actions, parent, false)
        return FunctionsAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return 11
    }

    override fun onBindViewHolder(holder: FunctionsAdapterHolder, position: Int) {
        val view: DrawView? = BlankFragment.view
        BlankFragment.view?.currEdge = null
        BlankFragment.view?.vertexTmp = null

        holder.kartinka.setImageDrawable(imageViews[position])


        holder.itemView.setOnClickListener {
            if (position == 8) {
                Toast.makeText(
                    context,
                    String.valueOf(view?.graph?.countEdge),
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (position == 7) {
                Toast.makeText(
                    context,
                    String.valueOf(view?.graph?.countVertex),
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (position == 6) {
                val strings = arrayOf("text/*")
               // MainActivity.launcher.launch(strings)
            }
            if (position == 5) {
               // MainActivity.launcherSave.launch("graph.txt")
            }
            if (position == 4) {
                view?.clear()
            } else {
                currentStates = States.getState(position)!!
            }
        }
    }

    inner class FunctionsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kartinka: ImageView

        init {
            kartinka = itemView.findViewById(R.id.img)
        }
    }

    companion object {
        var currentStates: States = States.ADD_POINT
    }
}
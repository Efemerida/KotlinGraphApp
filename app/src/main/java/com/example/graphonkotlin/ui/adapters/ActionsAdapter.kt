package com.example.graphonkotlin.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.graphonkotlin.R
import com.example.graphonkotlin.ui.BlankFragment
import com.example.graphonkotlin.ui.MainActivity
import com.example.graphonkotlin.ui.States
import com.example.graphonkotlin.ui.Utils
import com.example.graphonkotlin.ui.services.FIleService
import java.lang.String
import kotlin.Int
import kotlin.arrayOf

class ActionsAdapter(
    context: Context,
    var imageViews: MutableList<Drawable?>,
    onStateClickListener: OnStateClickListener
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
        return 13
    }

    override fun onBindViewHolder(holder: FunctionsAdapterHolder, position: Int) {

        var view  = BlankFragment.viewTmp
        BlankFragment.viewTmp?.currEdge = null
        BlankFragment.viewTmp?.vertexTmp = null

        holder.kartinka.setImageDrawable(imageViews[position])


        holder.itemView.setOnClickListener {
            Log.d("tagg",""+position);

            if (position == 8) {
                Toast.makeText(
                    context,
                    String.valueOf(view?.graph?.countEdge),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (position == 7) {
                Toast.makeText(
                    context,
                    String.valueOf(view?.graph?.countVertex),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (position == 6) {
                view?.clearpath()
                val strings = arrayOf("text/*")
                MainActivity.launcher.launch(strings)
            }
            else if (position == 5) {
                MainActivity.launcherSave.launch("graph.txt")
            }
            else if (position == 4) {
                view?.clearpath()
                view?.clear()


            }else if(position == 11){
                view?.clearpath()
                val alertWeight = AlertDialog.Builder(this.context)
                val edt = EditText(this.context)
                edt.filters= arrayOf(Utils.InputFilterMinMax(1, view?.vertexes?.size ?: 0))
                alertWeight.setTitle(R.string.alert_to_set_amount_title)
                alertWeight.setView(edt)
                val layoutalert = LinearLayout(this.context)
                layoutalert.setOrientation(LinearLayout.VERTICAL)
                layoutalert.addView(edt)
                alertWeight.setView(layoutalert)
                alertWeight.setPositiveButton(
                    R.string.accept_rus
                ) { dialog, which ->
                    view?.drawPath(edt)
                    view?.checkPath()
                    view?.invalidate()
                }
                alertWeight.setNegativeButton(
                    R.string.deny_rus
                ) { dialog, which ->
                    dialog.cancel()
                    view?.invalidate()
                }
                alertWeight.show()
            }else if(position == 12){
                view?.clearpath()
            }
            else {
                view?.clearpath()
                currentStates = States.getState(position)!!
                Log.d("asda", currentStates.toString());
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
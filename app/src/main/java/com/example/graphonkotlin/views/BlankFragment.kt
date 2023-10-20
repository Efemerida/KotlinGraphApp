package com.example.graphonkotlin.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.example.graphonkotlin.R

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        var myView = DrawView(context)
        myView.setBackgroundColor(R.color.black)
        Companion.view = myView
        Log.d("tagg","ssss create");
        return myView
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var view: DrawView? = null

    }
}
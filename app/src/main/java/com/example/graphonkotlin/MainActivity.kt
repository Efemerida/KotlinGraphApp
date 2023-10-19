package com.example.graphonkotlin

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.graphonkotlin.adapters.ActionsAdapter
import com.example.graphonkotlin.databinding.ActivityMainClassBinding
import com.example.graphonkotlin.entities.Graph
import com.example.graphonkotlin.services.FIleService
import com.example.graphonkotlin.utils.States
import com.example.graphonkotlin.views.BlankFragment
import com.example.graphonkotlin.views.DrawView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainClassBinding

    companion object{
        lateinit var drawView:DrawView
        lateinit var pathSave: Uri
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main_class)

        drawView = DrawView(this)

        val open = registerForActivityResult<Array<String>, Uri>(
            ActivityResultContracts.OpenDocument(), ActivityResultCallback<Uri> { url: Uri? ->
                val fIleService = FIleService(this)
                val view: DrawView? = BlankFragment.view
                    val graphCode: String = fIleService.loadGraph(url!!, this)
                    view?.loadGraph(Graph.loadGraph(graphCode))
            }
        )
        val create = registerForActivityResult<String, Uri>(
            ActivityResultContracts.CreateDocument(), ActivityResultCallback<Uri> { url: Uri ->
                val fIleService = FIleService(this)
                val view: DrawView? = BlankFragment.view
                fIleService.saveGraph( view!!.graph, url)
                pathSave = url
            }
        )


        val recyclerView: RecyclerView = binding.recyclerView;
        val drawable1: Drawable? = getDrawable(R.drawable.addpoint)
        val drawable2: Drawable? = getDrawable(R.drawable.addline)
        val drawable3: Drawable? = getDrawable(R.drawable.deleteline)
        val drawable4: Drawable? = getDrawable(R.drawable.deletepoint)
        val drawable5: Drawable? = getDrawable(R.drawable.clear)
        val drawable6: Drawable? = getDrawable(R.drawable.upload)
        val drawable7: Drawable? = getDrawable(R.drawable.download)
        val drawable8: Drawable? = getDrawable(R.drawable.sumpoints)
        val drawable9: Drawable? = getDrawable(R.drawable.sumlines)
        val drawable10: Drawable? = getDrawable(R.drawable.hasconnection)
        val drawable11: Drawable? = getDrawable(R.drawable.wt)
        val imageViews: MutableList<Drawable?> = ArrayList()
        imageViews.add(drawable1)
        imageViews.add(drawable2)
        imageViews.add(drawable3)
        imageViews.add(drawable4)
        imageViews.add(drawable5)
        imageViews.add(drawable6)
        imageViews.add(drawable7)
        imageViews.add(drawable8)
        imageViews.add(drawable9)
        imageViews.add(drawable10)
        imageViews.add(drawable11)


        val onStateClickListener: ActionsAdapter.OnStateClickListener = object : ActionsAdapter.OnStateClickListener{
            override fun onStateClick(position: Int) {
                ActionsAdapter.currentStates = States.getState(position)!!
            }
        }
        recyclerView.adapter = ActionsAdapter(this, imageViews)
    }


}
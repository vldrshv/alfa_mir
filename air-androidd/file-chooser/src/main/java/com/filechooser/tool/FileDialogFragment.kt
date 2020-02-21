package com.filechooser.tool

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.filechooser.R
import java.io.File

class FileDialogFragment : androidx.fragment.app.DialogFragment() {

    var adapter = FileAdapter()
        private set

    private var path: TextView? = null
    private var listener = View.OnClickListener { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Light)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.file_chooser_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.file_recycler)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        path = view.findViewById(R.id.path)

        view.findViewById<Button>(R.id.send)?.also { buttonSend ->
            buttonSend.tag = "send"
            buttonSend.setOnClickListener(listener)
        }

        view.findViewById<Button>(R.id.cancel)?.also { buttonCancel ->
            buttonCancel.tag = "cancel"
            buttonCancel.setOnClickListener(listener)
        }
    }

    fun setFiles(files: List<File>, selected: List<Int>) {
        adapter.setFiles(files, selected)
    }

    fun displayPath(path: String) {
        this.path?.also {
            it.text = path
        }
    }

    fun setOnItemClickListener(listener: View.OnClickListener) {
        adapter.setOnClickListener(listener)
    }

    fun setOnActionClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }
}
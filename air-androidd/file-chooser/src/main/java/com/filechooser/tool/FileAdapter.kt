@file:Suppress("DEPRECATION")

package com.filechooser.tool

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.filechooser.R
import com.filechooser.internals.FileUtil
import java.io.File

class FileAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<FileVH>() {

    private lateinit var context: Context
    private var files: List<File> = listOf()
    private var selected: List<Int> = listOf()
    private var listener = View.OnClickListener { }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FileVH {
        context = parent.context
        return FileVH(LayoutInflater.from(parent.context).inflate(R.layout.file_vh, parent, false))
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: FileVH, position: Int) {
        val file = files[position]
        holder.name.text = file.name
        holder.size.text = FileUtil.getReadableFileSize(file.length())
        holder.layout.tag = position
        holder.layout.setOnClickListener(listener)
        val isSelected = selected.contains(position)
        val resources = context.resources

        if (isSelected) {
            holder.icon.setImageDrawable(context.getDrawable(R.drawable.selected))
            holder.layout.setBackgroundColor(resources.getColor(R.color.light))
            holder.iconContainer.background.setTint(resources.getColor(R.color.white))

        } else {
            holder.icon.setImageDrawable(if (file.isFile)
                context.getDrawable(R.drawable.ic_file_v) else
                context.getDrawable(R.drawable.ic_folder_v))
            holder.layout.setBackgroundColor(resources.getColor(R.color.white))
            holder.iconContainer.background.setTint(resources.getColor(R.color.light))
        }
    }

    fun setFiles(files: List<File>, selected: List<Int>) {
        this.files = files
        this.selected = selected
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }
}

class FileVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    var icon: ImageView = view.findViewById(R.id.icon)
    var name: TextView = view.findViewById(R.id.name)
    var size: TextView = view.findViewById(R.id.size)
    var layout: LinearLayout = view.findViewById(R.id.layout)
    var iconContainer: androidx.cardview.widget.CardView = view.findViewById(R.id.icon_container)
}
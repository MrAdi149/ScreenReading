package com.example.screenarrator.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.example.screenarrator.R
import com.example.screenarrator.extensions.getSpannable
import com.example.screenarrator.extensions.setVisible
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.model.Header
import com.example.screenarrator.model.Item
import com.example.screenarrator.model.Training

fun headerAdapterDelegate() = adapterDelegate<Header,Any>(R.layout.view_header){
    val header: TextView = findViewById(R.id.headerView)

    bind{
        header.text = item.title(context)
        Accessibility.heading(header, true)
    }
}

fun textAdapterDelegate() = adapterDelegate<String, Any>(R.layout.view_text){
    val textView: TextView = findViewById(R.id.textView)

    bind{
        textView.text = item
    }
}

fun textResourceAdapterDelegate() = adapterDelegate<Int, Any>(R.layout.view_text){
    val textView: TextView = findViewById(R.id.textView)
    val marginMedium = context.resources.getDimension(R.dimen.margin_medium).toInt()
    val marginSmall = context.resources.getDimension(R.dimen.margin_small).toInt()

    bind {
        textView.text = context.getSpannable(item)
        when(adapterPosition){
            0-> textView.setPadding(marginMedium)
            -1-> textView.setPadding(marginMedium)
            else -> textView.setPadding(marginMedium, 0, marginMedium, marginSmall)
        }
    }
}

inline fun <reified T: Item> itemAdapterDelegate(crossinline callback: (T) -> Unit) =
    adapterDelegate<T,Any>(R.layout.view_item){
        val textView: TextView = findViewById(R.id.textView)
        itemView.setOnClickListener{
            callback(item)
        }

        Accessibility.button(itemView)

        bind {
            textView.text = item.title(context)
        }
    }

inline fun <reified T: Training> trainingAdapterDelegate(crossinline callback: (T) -> Unit) =
    adapterDelegate<T, Any>(R.layout.view_training){
        val textView: TextView = findViewById(R.id.textView)
        val imageView: ImageView = findViewById(R.id.imageView)

        itemView.setOnClickListener{
            callback(item)
        }

        bind{
            val title = item.title(context)
            val completed = item.completed(context)

            textView.text = title

            Accessibility.label(itemView, title)
            Accessibility.button(itemView, true)

            if(completed){
                imageView.setVisible(true)
                Accessibility.state(itemView, context.getSpannable(R.string.action_completed))
            }else{
                imageView.setVisible(false)
                Accessibility.state(itemView, null)
            }
        }
    }
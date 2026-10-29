package com.example.notasapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.R
import com.example.notasapp.model.Nota

class NotasAdapter : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {
    private var notas = listOf<Nota>()

    class NotaViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val titulo: TextView = vista.findViewById(R.id.itemTitulo)
        val contenido: TextView = vista.findViewById(R.id.itemContenido)
        val fecha: TextView = vista.findViewById(R.id.itemFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.titulo.text = nota.titulo
        holder.contenido.text = nota.contenido
        holder.fecha.text = nota.fecha_creacion
    }

    override fun getItemCount(): Int = notas.size

    fun actualizar(nuevas: List<Nota>) {
        notas = nuevas
        notifyDataSetChanged()
    }
}

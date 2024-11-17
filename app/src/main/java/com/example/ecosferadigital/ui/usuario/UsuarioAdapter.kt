package com.example.ecosferadigital.ui.usuario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosferadigital.R
import com.example.ecosferadigital.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioAdapter(
    private val usuarios: List<Usuario>,
    private val onEdit: (Usuario) -> Unit,
    private val onDelete: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvEndereco: TextView = itemView.findViewById(R.id.tvEndereco)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvTelefone: TextView = itemView.findViewById(R.id.tvTelefone)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.tvNome.text = usuario.nome
        holder.tvEndereco.text = usuario.endereco
        holder.tvEmail.text = usuario.email
        holder.tvTelefone.text = usuario.telefone

        holder.btnEdit.setOnClickListener {
            onEdit(usuario)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(usuario)
        }
    }

    override fun getItemCount(): Int = usuarios.size
}

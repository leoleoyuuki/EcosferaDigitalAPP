package com.example.ecosferadigital.ui.usuario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosferadigital.databinding.FragmentUsuarioListarBinding
import com.example.ecosferadigital.models.Usuario
import com.example.ecosferadigital.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UsuarioListarFragment : Fragment() {

    private var _binding: FragmentUsuarioListarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUsuarioListarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        adapter = UsuarioAdapter(usuarios, { usuario -> editarUsuario(usuario) }, { usuario -> excluirUsuario(usuario) })
        binding.rvUsuarios.adapter = adapter

        carregarUsuarios()
    }

    private fun carregarUsuarios() {
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUsuarios()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                usuarios.clear()
                usuarios.addAll(response.body()!!)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Falha ao carregar usuários", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editarUsuario(usuario: Usuario) {
        // Implementar navegação para o fragment de edição passando o usuário selecionado
        // Exemplo:
        // findNavController().navigate(R.id.action_usuarioListarFragment_to_usuarioEditarFragment, Bundle().apply {
        //     putParcelable("usuarioId", usuario.id)
        // })
        Toast.makeText(requireContext(), "Editar usuário: ${usuario.nome}", Toast.LENGTH_SHORT).show()
    }

    private fun excluirUsuario(usuario: Usuario) {
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.deleteUsuario(usuario.id)
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Usuário excluído com sucesso!", Toast.LENGTH_SHORT).show()
                carregarUsuarios()
            } else {
                Toast.makeText(requireContext(), "Falha ao excluir usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
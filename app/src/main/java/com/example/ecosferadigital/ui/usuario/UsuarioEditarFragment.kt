package com.example.ecosferadigital.ui.usuario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ecosferadigital.databinding.FragmentUsuarioEditarBinding
import com.example.ecosferadigital.models.UsuarioPost
import com.example.ecosferadigital.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UsuarioEditarFragment : Fragment() {

    private var _binding: FragmentUsuarioEditarBinding? = null
    private val binding get() = _binding!!
    private var usuarioId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt("usuarioId")
            // Carregar dados do usuário via API se necessário
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUsuarioEditarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuarioId?.let { id ->
            carregarUsuario(id)
        }

        binding.btnAtualizar.setOnClickListener {
            atualizarUsuario()
        }
    }

    private fun carregarUsuario(id: Int) {
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUsuarioById(id)
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val usuario = response.body()!!
                binding.etNome.setText(usuario.nome)
                binding.etEndereco.setText(usuario.endereco)
                binding.etEmail.setText(usuario.email)
                binding.etTelefone.setText(usuario.telefone)
            } else {
                Toast.makeText(requireContext(), "Falha ao carregar usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun atualizarUsuario() {
        val nome = binding.etNome.text.toString().trim()
        val endereco = binding.etEndereco.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefone = binding.etTelefone.text.toString().trim()

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioAtualizado = UsuarioPost(
            nome = nome,
            endereco = endereco,
            email = email,
            telefone = telefone
        )

        usuarioId?.let { id ->
            lifecycleScope.launch {
                val response = try {
                    RetrofitInstance.api.updateUsuario(id, usuarioAtualizado)
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@launch
                } catch (e: HttpException) {
                    Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Falha ao atualizar usuário", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

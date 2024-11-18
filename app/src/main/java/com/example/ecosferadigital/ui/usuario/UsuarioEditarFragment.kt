package com.example.ecosferadigital.ui.usuario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecosferadigital.databinding.FragmentUsuarioEditarBinding
import com.example.ecosferadigital.models.Usuario
import com.example.ecosferadigital.models.UsuarioPost
import com.example.ecosferadigital.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UsuarioEditarFragment : Fragment() {

    private var _binding: FragmentUsuarioEditarBinding? = null
    private val binding get() = _binding!!
    private var usuarioId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera o ID do usuário do Bundle
        arguments?.let {
            usuarioId = it.getInt("usuarioId")
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
        super.onViewCreated(view, savedInstanceState)

        if (usuarioId != null) {
            carregarDadosDoUsuario(usuarioId!!)
        } else {
            Toast.makeText(requireContext(), "Erro ao carregar ID do usuário", Toast.LENGTH_SHORT).show()
        }

        binding.btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarDadosDoUsuario(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = try {
                RetrofitInstance.api.getUsuarioById(id) // Ajuste conforme sua API
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


    private fun salvarAlteracoes() {
        val nome = binding.etNome.text.toString()
        val endereco = binding.etEndereco.text.toString()
        val email = binding.etEmail.text.toString()
        val telefone = binding.etTelefone.text.toString()

        if (usuarioId != null) {
            val usuarioPost = UsuarioPost(
                nome = nome,
                endereco = endereco,
                email = email,
                telefone = telefone
            )

            CoroutineScope(Dispatchers.Main).launch {
                val response = try {
                    RetrofitInstance.api.updateUsuario(usuarioId!!, usuarioPost)
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@launch
                } catch (e: HttpException) {
                    Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Falha ao atualizar usuário", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "ID do usuário inválido", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

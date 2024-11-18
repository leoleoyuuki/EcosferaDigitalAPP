package com.example.ecosferadigital.ui.usuario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ecosferadigital.databinding.FragmentUsuarioInserirBinding
import com.example.ecosferadigital.models.UsuarioPost
import com.example.ecosferadigital.network.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UsuarioInserirFragment : Fragment() {

    private var _binding: FragmentUsuarioInserirBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUsuarioInserirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnInserir.setOnClickListener {
            inserirUsuario()
        }
    }

    private fun inserirUsuario() {
        val nome = binding.etNome.text.toString().trim()
        val endereco = binding.etEndereco.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefone = binding.etTelefone.text.toString().trim()

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = UsuarioPost(
            nome = nome,
            endereco = endereco,
            email = email,
            telefone = telefone
        )

        // Enviar para a API .NET
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.createUsuario(usuario)
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.i("ERROR","${e.message}")
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.i("ERROR", "${e.message}")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(requireContext(), "Usuário inserido com sucesso!", Toast.LENGTH_SHORT).show()
                limparCampos()
            } else {
                Toast.makeText(requireContext(), "Falha ao inserir usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limparCampos() {
        binding.etNome.text.clear()
        binding.etEndereco.text.clear()
        binding.etEmail.text.clear()
        binding.etTelefone.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

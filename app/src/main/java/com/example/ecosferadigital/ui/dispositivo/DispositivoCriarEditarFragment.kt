package com.example.ecosferadigital.ui.dispositivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecosferadigital.databinding.FragmentDispositivoCriarEditarBinding
import com.example.ecosferadigital.models.DispositivoPost
import com.example.ecosferadigital.network.ApiService
import com.example.ecosferadigital.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DispositivoCriarEditarFragment : Fragment() {

    private lateinit var binding: FragmentDispositivoCriarEditarBinding
    private var usuarioId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDispositivoCriarEditarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSalvar.setOnClickListener {
            if (usuarioId == null) {
                Toast.makeText(requireContext(), "ID do usuário não encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipoDispositivo = binding.etTipoDispositivo.text.toString().trim()
            val descricao = binding.etDescricao.text.toString().trim()
            val status = binding.etStatus.text.toString().trim()

            if (tipoDispositivo.isEmpty() || descricao.isEmpty() || status.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dispositivoPost = DispositivoPost(
                usuarioId = usuarioId!!,
                tipoDispositivo = tipoDispositivo,
                descricao = descricao,
                status = status
            )

            enviarDispositivo(dispositivoPost)
        }
    }

    private fun enviarDispositivo(dispositivoPost: DispositivoPost) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.createDispositivo(dispositivoPost)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Dispositivo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack() // Voltar para o fragmento anterior
                    } else {
                        Toast.makeText(requireContext(), "Erro ao salvar dispositivo: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Erro ao salvar dispositivo: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

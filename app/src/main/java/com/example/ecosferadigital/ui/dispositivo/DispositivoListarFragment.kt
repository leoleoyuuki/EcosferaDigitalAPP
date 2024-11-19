package com.example.ecosferadigital.ui.dispositivo

import DispositivoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosferadigital.R
import com.example.ecosferadigital.databinding.FragmentDispositivoListarBinding
import com.example.ecosferadigital.models.Dispositivo
import com.example.ecosferadigital.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DispositivoListarFragment : Fragment() {

    private lateinit var binding: FragmentDispositivoListarBinding
    private lateinit var adapter: DispositivoAdapter
    private val dispositivos = mutableListOf<Dispositivo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDispositivoListarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurando o RecyclerView com o adapter
        adapter = DispositivoAdapter(dispositivos) { dispositivo ->
            excluirDispositivo(dispositivo)
        }
        binding.rvDispositivos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDispositivos.adapter = adapter


        carregarDispositivos()
    }

    private fun carregarDispositivos() {
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getDispositivos()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                dispositivos.clear()
                dispositivos.addAll(response.body()!!)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Falha ao carregar dispositivos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun excluirDispositivo(dispositivo: Dispositivo) {
        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.deleteDispositivo(dispositivo.id)
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Erro HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Dispositivo excluído com sucesso!", Toast.LENGTH_SHORT).show()
                carregarDispositivos()
            } else {
                Toast.makeText(requireContext(), "Falha ao excluir usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirCriarEditarDispositivoFragment() {
        val fragment = DispositivoCriarEditarFragment()
        val args = Bundle().apply {
            putInt("usuarioId", 1) // Substituir pelo ID real do usuário selecionado
        }
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

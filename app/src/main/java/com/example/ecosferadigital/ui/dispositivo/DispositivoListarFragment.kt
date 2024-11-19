package com.example.ecosferadigital.ui.dispositivo

import DispositivoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosferadigital.R
import com.example.ecosferadigital.databinding.FragmentDispositivoListarBinding
import com.example.ecosferadigital.models.Dispositivo

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

        // Botão para adicionar um novo dispositivo
        binding.btnAdicionarDispositivo.setOnClickListener {
            abrirCriarEditarDispositivoFragment()
        }

        carregarDispositivos()
    }

    private fun carregarDispositivos() {
        // Simular carregamento de dispositivos
        dispositivos.clear() // Limpa antes de adicionar para evitar duplicados
        dispositivos.addAll(
            listOf(
                Dispositivo(1, 1, "Sensor de Temperatura", "Sala 1", "Ativo"),
                Dispositivo(2, 1, "Sensor de Umidade", "Sala 2", "Inativo")
            )
        )
        adapter.notifyDataSetChanged() // Atualiza o RecyclerView
    }

    private fun excluirDispositivo(dispositivo: Dispositivo) {
        dispositivos.remove(dispositivo)
        adapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "Dispositivo excluído: ${dispositivo.tipoDispositivo}", Toast.LENGTH_SHORT).show()
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

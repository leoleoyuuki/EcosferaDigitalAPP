package com.example.ecosferadigital.ui.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosferadigital.R
import com.example.ecosferadigital.databinding.FragmentSensorListBinding
import com.example.ecosferadigital.models.Sensor
import com.example.ecosferadigital.ui.sensor.adapter.SensorAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SensorListFragment : Fragment() {

    private var _binding: FragmentSensorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SensorAdapter
    private val sensores = mutableListOf<Sensor>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSensors.layoutManager = LinearLayoutManager(requireContext())

        // Configurar o adapter com os callbacks
        adapter = SensorAdapter(
            sensores,
            onEdit = { sensor -> editarSensor(sensor) },
            onDelete = { sensor -> excluirSensor(sensor) }
        )

        binding.rvSensors.adapter = adapter

        carregarSensores()
    }

    private fun carregarSensores() {
        val db = FirebaseFirestore.getInstance()
        val sensorsRef = db.collection("sensors")

        sensorsRef.get()
            .addOnSuccessListener { result ->
                val sensoresList = mutableListOf<Sensor>()
                for (document in result) {
                    val sensor = document.toObject(Sensor::class.java)
                    sensoresList.add(sensor)
                }
                // Atualiza a lista de sensores no adapter
                adapter.submitList(sensoresList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Erro ao carregar sensores: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editarSensor(sensor: Sensor) {
        val sensorEditarFragment = SensorEditFragment()

        // Cria o bundle com os dados do sensor
        val args = Bundle()
        args.putString("sensorName", sensor.name)  // Nome do sensor
        sensorEditarFragment.arguments = args

        // Realiza a transição para o fragmento de edição
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, sensorEditarFragment)  // Substitua "fragment_container" pelo ID do container principal
            .addToBackStack(null)  // Adiciona à pilha de navegação
            .commit()
    }



    private fun excluirSensor(sensor: Sensor) {
        val db = FirebaseFirestore.getInstance()

        // Buscar o documento pelo nome
        db.collection("sensors")
            .whereEqualTo("name", sensor.name)  // Buscando pelo nome do sensor
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // Supondo que o nome é único, pegamos o primeiro documento
                    val document = result.documents.first()

                    // Deletar o documento encontrado
                    document.reference.delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Sensor excluído com sucesso!", Toast.LENGTH_SHORT).show()
                            carregarSensores()  // Recarregar a lista após a exclusão
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Erro ao excluir sensor: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Sensor não encontrado para exclusão", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Erro ao buscar sensor: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


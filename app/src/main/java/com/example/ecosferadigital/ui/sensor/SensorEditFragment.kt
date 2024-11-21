package com.example.ecosferadigital.ui.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecosferadigital.R
import com.example.ecosferadigital.databinding.FragmentSensorEditBinding
import com.example.ecosferadigital.models.Sensor
import com.google.firebase.firestore.FirebaseFirestore

class SensorEditFragment : Fragment() {

    private var _binding: FragmentSensorEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorName: String
    private var sensorToEdit: Sensor? = null
    private val db = FirebaseFirestore.getInstance()  // Objeto para acessar o Firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensorEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera o nome do sensor passado pelo Bundle
        sensorName = arguments?.getString("sensorName") ?: ""

        if (sensorName.isNotEmpty()) {
            carregarSensorPorNome(sensorName)
        }

        // Salvar as edições
        binding.btnSave.setOnClickListener {
            val newName = binding.etSensorName.text.toString().trim()
            val newDescription = binding.etSensorDescription.text.toString().trim()

            if (newName.isNotEmpty() && newDescription.isNotEmpty()) {
                editarSensor(sensorName, newName, newDescription)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para carregar o sensor usando o nome
    private fun carregarSensorPorNome(nome: String) {
        val sensorsCollection = db.collection("sensors")

        // Buscar o sensor pelo nome (assumindo que "name" seja um campo no Firestore)
        sensorsCollection.whereEqualTo("name", nome).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val sensor = snapshot.documents[0].toObject(Sensor::class.java)
                    sensorToEdit = sensor
                    sensor?.let {
                        binding.etSensorName.setText(it.name)
                        binding.etSensorDescription.setText(it.description)
                    }
                } else {
                    Toast.makeText(requireContext(), "Sensor não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao carregar sensor", Toast.LENGTH_SHORT).show()
            }
    }

    // Função para editar e salvar as alterações no sensor
    private fun editarSensor(oldName: String, newName: String, newDescription: String) {
        val sensorsCollection = db.collection("sensors")

        // Buscar o sensor pelo nome
        sensorsCollection.whereEqualTo("name", oldName).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val documentId = snapshot.documents[0].id  // Pegando o ID do documento

                    // Atualizando o sensor no Firestore
                    val updatedSensor = hashMapOf(
                        "name" to newName,
                        "description" to newDescription
                    )

                    // Atualizando o documento com o novo nome e descrição
                    sensorsCollection.document(documentId).update(updatedSensor as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Sensor atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressed()  // Volta para o fragmento anterior
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Falha ao atualizar o sensor", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Sensor não encontrado para editar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao editar o sensor", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

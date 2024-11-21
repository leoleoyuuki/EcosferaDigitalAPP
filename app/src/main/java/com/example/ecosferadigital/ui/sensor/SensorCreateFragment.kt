package com.example.ecosferadigital.ui.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecosferadigital.databinding.FragmentSensorCreateBinding
import com.google.firebase.firestore.FirebaseFirestore

class SensorCreateFragment : Fragment() {

    private var _binding: FragmentSensorCreateBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSensorCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveSensor.setOnClickListener {
            val name = binding.etSensorName.text.toString()
            val type = binding.etSensorType.text.toString()
            val description = binding.etSensorDescription.text.toString()

            if (name.isNotEmpty() && type.isNotEmpty() && description.isNotEmpty()) {
                val sensor = mapOf(
                    "name" to name,
                    "type" to type,
                    "description" to description
                )

                firestore.collection("sensors")
                    .add(sensor)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Sensor adicionado!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Erro ao adicionar sensor", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

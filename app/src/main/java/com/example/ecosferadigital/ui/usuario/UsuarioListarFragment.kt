package com.example.ecosferadigital.ui.usuario
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
import com.example.ecosferadigital.databinding.FragmentUsuarioListarBinding
import com.example.ecosferadigital.models.Usuario
import com.example.ecosferadigital.network.RetrofitInstance
import com.example.ecosferadigital.ui.dispositivo.DispositivoCriarEditarFragment
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
        super.onViewCreated(view, savedInstanceState)

        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())

        // Configurar o adapter com os callbacks
        adapter = UsuarioAdapter(
            usuarios,
            onEdit = { usuario -> editarUsuario(usuario) },
            onDelete = { usuario -> excluirUsuario(usuario) },
            onCreateDevice = { usuario -> navegarParaCriarDispositivo(usuario.id) } // Corrigido
        )

        binding.rvUsuarios.adapter = adapter

        carregarUsuarios()
    }

    /**
     * Navega para o fragmento de criação de dispositivo, enviando o ID do usuário como argumento.
     */
    private fun navegarParaCriarDispositivo(usuarioId: Int) {
        val fragment = DispositivoCriarEditarFragment().apply {
            arguments = Bundle().apply {
                putInt("usuarioId", usuarioId)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
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
        val usuarioEditarFragment = UsuarioEditarFragment()

        // Cria o bundle com o ID do usuário
        val args = Bundle()
        args.putInt("usuarioId", usuario.id)
        usuarioEditarFragment.arguments = args

        // Realiza a transição para o fragmento de edição
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, usuarioEditarFragment) // Substitua "fragment_container" pelo ID do container principal
            .addToBackStack(null) // Adiciona à pilha de navegação
            .commit()
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

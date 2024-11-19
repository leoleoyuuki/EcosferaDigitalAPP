package com.example.ecosferadigital

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ecosferadigital.databinding.ActivityMainBinding
import com.example.ecosferadigital.ui.dispositivo.DispositivoListarFragment

import com.example.ecosferadigital.ui.usuario.UsuarioEditarFragment
import com.example.ecosferadigital.ui.usuario.UsuarioInserirFragment
import com.example.ecosferadigital.ui.usuario.UsuarioListarFragment
import com.google.android.material.navigation.NavigationView
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        val navView = binding.navView

        navView.setNavigationItemSelectedListener(this)

        // Tela padrão ao abrir o app
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsuarioListarFragment())
            .commit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_launcher_background)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.nav_usuario_list -> UsuarioListarFragment() // Lista de usuários
            R.id.nav_usuario_insert -> UsuarioInserirFragment() // Inserir usuários
            R.id.nav_usuario_edit -> UsuarioEditarFragment() // Editar usuários
            R.id.nav_dispositivo_list -> DispositivoListarFragment() // Lista de dispositivos
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, it)
                .commit()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

package com.example.ecosferadigital

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ecosferadigital.databinding.ActivityMainBinding

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

        // Setup Drawer Layout
        drawerLayout = binding.drawerLayout
        val navView = binding.navView

        // Set Navigation Item Selected Listener
        navView.setNavigationItemSelectedListener(this)

        // Default fragment (first screen after login)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsuarioListarFragment())
            .commit()

        // Setup ActionBar to open/close drawer
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_launcher_background) // Icon for menu
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.nav_usuario_list -> UsuarioListarFragment()  // Lista de usuários
            R.id.nav_usuario_insert -> UsuarioInserirFragment()  // Inserir usuários
            R.id.nav_usuario_edit -> UsuarioEditarFragment()  // Editar usuários
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

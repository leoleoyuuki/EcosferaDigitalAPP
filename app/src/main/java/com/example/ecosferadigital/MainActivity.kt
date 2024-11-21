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
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.ecosferadigital.ui.sensor.SensorCreateFragment
import com.example.ecosferadigital.ui.sensor.SensorListFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        val navView = binding.navView

        // Configura o NavigationView
        navView.setNavigationItemSelectedListener(this)

        // Configura o ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.navigation_drawer_open, // Texto para acessibilidade ao abrir o drawer
            R.string.navigation_drawer_close // Texto para acessibilidade ao fechar o drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Habilita o botão no ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Tela padrão ao abrir o app
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsuarioListarFragment())
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.nav_usuario_list -> UsuarioListarFragment() // Lista de usuários
            R.id.nav_usuario_insert -> UsuarioInserirFragment() // Inserir usuários
            R.id.nav_dispositivo_list -> DispositivoListarFragment() // Lista de dispositivos
            R.id.nav_sensor_list -> SensorListFragment() // Lista  sensor
            R.id.nav_sensor_add ->SensorCreateFragment() // Adicionar novo sensor
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
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}

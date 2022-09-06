package com.example.damoim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.my_custom_action_bar.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        supportActionBar?.let {
            setCustomActionBar()
        }

        titleTxt.text = "모임찾기"
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_menu_interest -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu_interest",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.navigation_menu_favorite_meet -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu_favorite_meet",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.navigation_menu_recent_meet -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu_recent_meet",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.navigation_menu__premium_meet -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu__premium_meet",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.navigation_menu_mk_charge_class -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu_mk_charge_class",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.navigation_menu_setting -> Toast.makeText(
                    applicationContext,
                    "Clicked navigation_menu_setting",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    fun setCustomActionBar () {
        val defActionBar = supportActionBar!!

        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolbar = defActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_option_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

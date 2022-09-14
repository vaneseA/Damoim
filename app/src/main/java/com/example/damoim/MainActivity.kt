package com.example.damoim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.damoim.adapters.BottomViewPagerAdapter
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_custom_action_bar.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mBottomAdapter: BottomViewPagerAdapter

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
                R.id.navigation_menu_interest ->
                startActivity(Intent(context, TestActivity::class.java))

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


        mBottomAdapter = BottomViewPagerAdapter(this)
        bottomViewPager.adapter = mBottomAdapter

        bottomViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bottomNav.menu.getItem(position).isChecked = true
                    titleTxt.text = when (position) {
                        0 -> "모임찾기"
                        1 -> "유료클래스"
                        2 -> "내모임"
                        else -> "주변검색"
                    }
                }
            })

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_tab1 -> bottomViewPager.currentItem = 0
                R.id.bnv_tab2 -> bottomViewPager.currentItem = 1
                R.id.bnv_tab3 -> bottomViewPager.currentItem = 2
                R.id.bnv_tab4 -> bottomViewPager.currentItem = 3
            }
            return@setOnItemSelectedListener true
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

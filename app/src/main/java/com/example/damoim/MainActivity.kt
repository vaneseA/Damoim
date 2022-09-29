package com.example.damoim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.damoim.adapters.BottomViewPagerAdapter
import com.example.damoim.auth.LoginActivity
import com.example.damoim.auth.SignupActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_custom_action_bar.*

class MainActivity : AppCompatActivity() {
    private var profileImg: CircleImageView? = null
    private var nicknameEdt: TextView? = null

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mBottomAdapter: BottomViewPagerAdapter

    private var auth: FirebaseAuth? = null


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
                startActivity(Intent(context, SignupActivity::class.java))

                R.id.navigation_menu_favorite_meet -> startActivity(Intent(
                    context,
                    LoginActivity::class.java))
                R.id.navigation_menu_recent_meet -> startActivity(Intent(context, SignupActivity::class.java))
                R.id.navigation_menu__premium_meet -> startActivity(Intent(context,
                    SignupActivity::class.java))
                R.id.navigation_menu_mk_charge_class -> startActivity(Intent(context,
                    SignupActivity::class.java))
                R.id.navigation_menu_setting -> startActivity(Intent(context,
                    SignupActivity::class.java))
                R.id.navigation_menu_logout ->
                    FirebaseAuth.getInstance().signOut()

            }
            true
        }
        val header = navView.getHeaderView(0)
        nicknameEdt = header.findViewById(R.id.nicknameTxt)
        profileImg = header.findViewById(R.id.profileImg)

        loadUserinfo(auth?.currentUser)

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

    fun loadUserinfo(user: FirebaseUser?) {
        if (user != null) {
            //네비게이션 헤더
            Glide.with(context)
                .load(GlobalData.loginUser!!.profileImageUrl)
                .into(profileImg!!)
            nicknameEdt?.text = GlobalData.loginUser!!.username

        }
    }

}

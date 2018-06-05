package com.example.bottomnavigation.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.bottomnavigation.R
import com.example.bottomnavigation.extension.*
import com.example.bottomnavigation.helper.*
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
// import jdk.nashorn.internal.runtime.ECMAException.getException
// import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password










class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val KEY_POSITION = "keyPosition"

    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME

    private lateinit var toolbar: Toolbar

    private var mAuth: FirebaseAuth? = null

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var prefs = getSharedPreferences("com.example.bottomnavigation", MODE_PRIVATE)

        mAuth = FirebaseAuth.getInstance()

        if (prefs.getBoolean("firstrun", true)) {
            runIntro()
            prefs.edit().putBoolean("firstrun", false).commit()
        } else {
            runApp(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.getCurrentUser()
        Log.d("currentUser", currentUser.toString())

        mAuth!!.createUserWithEmailAndPassword("julien@citytaps.org", "testtest")
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("firebase", "createUserWithEmail:success")
                        val user = mAuth!!.getCurrentUser()
                        Log.d("userCreated", user.toString())
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("firebase", "createUserWithEmail:failure", task.exception)
                        //Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                        //        Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                    // ...
                }
        // updateUI(currentUser)

    }

    fun runIntro() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
    }

    fun runApp(savedInstanceState: Bundle?) {
        restoreSaveInstanceState(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        setSupportActionBar(toolbar)
        initBottomNavigation()
        initFragment(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        // Store the current navigation position.
        outState?.putInt(KEY_POSITION, navPosition.id)
        super.onSaveInstanceState(outState)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navPosition = findNavigationPositionById(item.itemId)
        return switchFragment(navPosition)
    }

    private fun restoreSaveInstanceState(savedInstanceState: Bundle?) {
        // Restore the current navigation position.
        savedInstanceState?.also {
            val id = it.getInt(KEY_POSITION, BottomNavigationPosition.HOME.id)
            navPosition = findNavigationPositionById(id)
        }
    }

    private fun initBottomNavigation() {
        bottomNavigation.disableShiftMode() // Extension function
        bottomNavigation.active(navPosition.position)   // Extension function
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(BottomNavigationPosition.HOME)
    }

    /**
     * Immediately execute transactions with FragmentManager#executePendingTransactions.
     */
    private fun switchFragment(navPosition: BottomNavigationPosition): Boolean {
        val fragment = supportFragmentManager.findFragment(navPosition)
        if (fragment.isAdded) return false
        detachFragment()
        attachFragment(fragment, navPosition.getTag())
        supportFragmentManager.executePendingTransactions()
        return true
    }

    private fun FragmentManager.findFragment(position: BottomNavigationPosition): Fragment {
        return findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    private fun detachFragment() {
        supportFragmentManager.findFragmentById(R.id.container)?.also {
            supportFragmentManager.beginTransaction().detach(it).commit()
        }
    }

    private fun attachFragment(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            supportFragmentManager.beginTransaction().attach(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.container, fragment, tag).commit()
        }
        // Set a transition animation for this transaction.
        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

}

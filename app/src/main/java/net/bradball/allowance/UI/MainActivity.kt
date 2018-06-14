package net.bradball.allowance.UI

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import net.bradball.allowance.R

class MainActivity : AppCompatActivity(), KidListFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        fm.findFragmentById(R.id.kid_list_container) ?: fm.beginTransaction().add(R.id.kid_list_container, KidListFragment.newInstance()).commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

package com.example.bottomnavigation.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beust.klaxon.Klaxon
import com.example.bottomnavigation.R
import com.example.bottomnavigation.ui.ChallengeCreationActivity
import com.example.bottomnavigation.ui.MainActivity
import com.example.bottomnavigation.ui.item.ActualityItem
import com.example.bottomnavigation.ui.item.ChallengeInProgressItem
import com.example.bottomnavigation.ui.item.InvitationItem
import com.github.kittinunf.fuel.Fuel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_home)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var prefs = this.getActivity()!!.getSharedPreferences("com.example.bottomnavigation", AppCompatActivity.MODE_PRIVATE)
        val name = prefs.getString("name", "")
        labelName.text = name

        val image = prefs.getString("image", "")
        Log.d("image", image)
        if (image !== "") {
            Picasso.with(context)
                    .load(image)
                    .into(profileImage, object : Callback {
                        override fun onError() {

                        }

                        override fun onSuccess() {
                            Log.d("success", "success")
                            profileImage.setBackgroundResource(R.drawable.round_image_background )
                        }
                    })
        }

        // Display RecyclerView for challenges in progress
        displayChallengeInProgress(view)
        displayInvitations(view)
        displayActuality(view)

        addChallenge_button.setOnClickListener(View.OnClickListener{
            val intent = Intent(activity, ChallengeCreationActivity::class.java)
            startActivity(intent)
        })

    }

    fun displayChallengeInProgress(view: View) {
        view.recyclerViewChallengeInProgress.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        Log.d("wesh", "wesh")
        val itemAdapter = FastItemAdapter<ChallengeInProgressItem>()

        val items = arrayListOf<ChallengeInProgressItem>()
        items.add(ChallengeInProgressItem("jul", "3", "50", "km", "15" ))
        items.add(ChallengeInProgressItem("titre 2", "4", "20", "km", "10" ))

        items.forEach({
            itemAdapter.add(it)
        })

        view.recyclerViewChallengeInProgress.adapter = itemAdapter

        var prefs = this.getActivity()!!.getSharedPreferences("com.example.bottomnavigation", AppCompatActivity.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        val path = "http://stark-temple-99246.herokuapp.com/users/$email"
        Fuel.get(path)
                .responseString { request, response, result ->
                    result.fold({ d ->
                        class challenge (val name: String = "")
                        class user (val email: String = "", val challenges: List<challenge> = arrayListOf<challenge>(), val name: String = "")
                        val result = Klaxon().parse<user>(d)

                        //result.forEach({
                            //itemAdapter.add(it)
                        //})
                        Log.d("immmm", result!!.challenges[0].name)
                        //Log.d("resres", prefs.getString("name", ""))
                        //do something with data
                    }, { err ->
                        Log.d("responsehttp", "fuck")
                        //do something with error
                    })
                }
    }

    fun displayInvitations(view: View) {
        view.recyclerViewInvitations.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        val itemAdapter = FastItemAdapter<InvitationItem>()

        val items = arrayListOf<InvitationItem>()
        items.add(InvitationItem("Lucille", "100 pompes à un doigt", R.drawable.profil_lucille))
        items.add(InvitationItem("Veronique", "200 abdos tous les jours", R.drawable.profil_vero))

        items.forEach({
            itemAdapter.add(it)
        })

        view.recyclerViewInvitations.adapter = itemAdapter
    }


    fun getDrawable(@DrawableRes res:Int): Drawable? {
        val currentContext = context

        if(currentContext is Context) {
            return ContextCompat.getDrawable(currentContext, res)
        }

        return null
    }


    fun displayActuality(view: View) {
        view.recyclerViewActuality.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        val itemAdapter = FastItemAdapter<ActualityItem>()

        val items = arrayListOf<ActualityItem>()
        items.add(ActualityItem("Félicitation !", "Tu as terminé le challenge en seulement 3 jours !"))
        items.add(ActualityItem("Hey you!", "Bravo, tu t'es connecté deux fois cette semaine."))

        items.forEach({
            itemAdapter.add(it)
        })

        view.recyclerViewActuality.adapter = itemAdapter
    }

}
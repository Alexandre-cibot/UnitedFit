package com.example.bottomnavigation.ui.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bottomnavigation.R
import com.example.bottomnavigation.ui.item.ActualityItem
import com.example.bottomnavigation.ui.item.ChallengeInProgressItem
import com.example.bottomnavigation.ui.item.InvitationItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
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

        // Display RecyclerView for challenges in progress
        displayChallengeInProgress(view)
        displayInvitations(view)
        displayActuality(view)

    }

    fun displayChallengeInProgress(view: View) {
        view.recyclerViewChallengeInProgress.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        val itemAdapter = FastItemAdapter<ChallengeInProgressItem>()

        for (i in 0..2) {
            itemAdapter.add(ChallengeInProgressItem())
        }

        view.recyclerViewChallengeInProgress.adapter = itemAdapter
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
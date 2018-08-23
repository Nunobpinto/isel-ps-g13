package isel.ps.eduwikimobile.ui.fragments.single

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.paramsContainer.OrganizationParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.organization_fragment.*

class OrganizationFragment : Fragment() {

    lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        val activity = activity as MainActivity
        activity.toolbar.title = "Organization"
        activity.toolbar.subtitle = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.organization_fragment, container, false)

        fetchOrganizationInfo()

        return view
    }

    private fun fetchOrganizationInfo() {
        AppController.actionHandler(
                AppController.ORGANIZATION,
                OrganizationParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { organization ->
                            organization_name.text = organization.fullName
                            organization_address.text = organization.address
                            organization_contacts.text = organization.contact
                            val activity = activity as MainActivity
                            activity.toolbar.title = organization.shortName
                            organization_progress_bar.visibility = View.GONE
                            organization_name.visibility = View.VISIBLE
                            organization_address.visibility = View.VISIBLE
                            organization_contacts.visibility = View.VISIBLE
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

}
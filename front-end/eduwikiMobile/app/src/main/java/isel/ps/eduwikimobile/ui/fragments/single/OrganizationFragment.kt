package isel.ps.eduwikimobile.ui.fragments.single

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.OrganizationParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.organization_fragment.*

class OrganizationFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = mainActivity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.organization_fragment, container, false)
        view.findViewById<ProgressBar>(R.id.organization_progress_bar).visibility = View.VISIBLE
        getOrganizationInfo()
        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getOrganizationInfo() {
        app.controller.actionHandler(
                AppController.ORGANIZATION,
                OrganizationParametersContainer(
                        app = app,
                        successCb = { organization ->
                            organization_name.text = organization.fullName
                            organization_insert_address.text = organization.address
                            organization_insert_contacts.text = organization.contact
                            organization_insert_website.text = organization.website
                            organization_progress_bar.visibility = View.GONE
                            organization_name.visibility = View.VISIBLE
                            organization_insert_address.visibility = View.VISIBLE
                            organization_insert_contacts.visibility = View.VISIBLE
                            organization_insert_website.visibility = View.VISIBLE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            else {
                                organization_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "Error", LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

}
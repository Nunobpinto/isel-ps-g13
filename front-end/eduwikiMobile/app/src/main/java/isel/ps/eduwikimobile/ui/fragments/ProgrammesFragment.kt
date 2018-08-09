package isel.ps.eduwikimobile.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.controller.parametersContainer.ProgrammeParametersContainer

class ProgrammesFragment : Fragment() {

    lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.programmes_fragment, container, false)
        val btn = view.findViewById<Button>(R.id.btn_all_programmes)

        btn.setOnClickListener {
            AppController.actionHandler(
                    AppController.ALL_PROGRAMMES,
                    ProgrammeParametersContainer(
                            app = activity.applicationContext as EduWikiApplication,
                            successCb = { p -> Toast.makeText(app, "Success ", LENGTH_LONG).show() },
                            errorCb = {error -> Toast.makeText(app, "Error", LENGTH_LONG).show()}
                    )
            )
        }
        return view
    }


}
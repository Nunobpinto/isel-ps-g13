package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.Programme
import isel.ps.eduwikimobile.ui.IDataComunication

class ProgrammeInfoTabFragment : Fragment() {

    private lateinit var dataComunication: IDataComunication
    private lateinit var programme: Programme

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.programme_info_tab_fragment, container, false)
        programme = dataComunication.getProgramme()!!

        val programmeAcademicDegree = view.findViewById<TextView>(R.id.academic_degree)
        val programmeDuration = view.findViewById<TextView>(R.id.duration)
        val programmeTotalCredits = view.findViewById<TextView>(R.id.total_credits)

        programmeAcademicDegree.text = programme.academicDegree
        programmeDuration.text = "${programme.duration} semesters"
        programmeTotalCredits.text = programme.totalCredits.toString()

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            dataComunication = context as IDataComunication
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }

}
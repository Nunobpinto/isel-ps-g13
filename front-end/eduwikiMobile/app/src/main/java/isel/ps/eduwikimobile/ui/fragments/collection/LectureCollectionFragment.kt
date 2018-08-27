package isel.ps.eduwikimobile.ui.fragments.collection

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseClassListAdapter
import isel.ps.eduwikimobile.adapters.LectureListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.CourseClass
import isel.ps.eduwikimobile.domain.model.single.Lecture
import isel.ps.eduwikimobile.paramsContainer.LectureCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.lecture_collection_fragment.*

class LectureCollectionFragment: Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var lectureList: MutableList<Lecture>
    private lateinit var lectureAdapter: LectureListAdapter
    lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        lectureList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lecture_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.lectures_recycler_view)

        val courseClass = dataComunication.getCourseClass()

        if (lectureList.size > 0) {
            lectureList.clear()
        }

        fetchLecturesOfCourseClass(courseClass!!.courseId, courseClass.classId)

        lectureAdapter = LectureListAdapter(context, lectureList)
        recyclerView.adapter = lectureAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchLecturesOfCourseClass(courseId: Int, classId: Int) {
        app.controller.actionHandler(
                AppController.ALL_LECTURES_OF_COURSE_CLASS,
                LectureCollectionParametersContainer(
                        courseId = courseId,
                        classId = classId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { lectures ->
                            lectureList.addAll(lectures.lectureList)
                            lectureAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            lectures_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
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
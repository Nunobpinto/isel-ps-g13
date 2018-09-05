import React from 'react'
import Profile from './components/users/Profile'
import OtherUser from './components/users/OtherUser'
import OrganizationVersion from './components/organization/OrganizationVersion'
import ProgrammeVersion from './components/programmes/ProgrammeVersion'
import CourseVersion from './components/courses/CourseVersion'
import Organization from './components/organization/Organization'
import Programmes from './components/programmes/Programmes'
import Programme from './components/programmes/Programme'
import Register from './components/auth/Register'
import Courses from './components/courses/Courses'
import Classes from './components/classes/Classes'
import Course from './components/courses/Course'
import Class from './components/classes/Class'
import ClassReports from './components/classes/ClassReports.js'
import ClassVersion from './components/classes/ClassVersion'
import Homework from './components/homeworks/Homework'
import Lecture from './components/lectures/Lecture'
import Exam from './components/exams/Exam'
import ExamReports from './components/exams/ExamReports'
import WorkAssignment from './components/workAssignments/WorkAssignment'
import Home from './components/home/Home'
import Logout from './components/auth/Logout'
import CourseClass from './components/courseClass/CourseClass'
import ProtectedRoute from './components/auth/ProtectedRoute'
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom'
import OrganizationReports from './components/organization/OrganizationReports'
import ProgrammeReports from './components/programmes/ProgrammeReports'
import CourseProgrammeVersion from './components/courseProgrammes/CourseProgrammeVersion'
import CourseProgrammeReports from './components/courseProgrammes/CourseProgrammeReports'
import CourseProgrammePage from './components/courseProgrammes/CourseProgrammePage'
import CourseReports from './components/courses/CourseReports'
import AllExamsPage from './components/exams/AllExamsPage'
import ExamVersion from './components/exams/ExamVersion'
import AllWorkAssignmentsPage from './components/workAssignments/AllWorkAssignmentsPage'
import WorkAssignmentReports from './components/workAssignments/WorkAssignmentReports'
import WorkAssignmentVersion from './components/workAssignments/WorkAssignmentVersion'
import CourseClassReports from './components/courseClass/CourseClassReports'
import AllHomeworksPage from './components/homeworks/AllHomeworksPage'
import HomeworkReports from './components/homeworks/HomeworkReports'
import HomeworkVersion from './components/homeworks/HomeworkVersion'
import LectureReports from './components/lectures/LectureReports'
import LectureVersion from './components/lectures/LectureVersion'
import AllLecturesPage from './components/lectures/AllLecturesPage'

export default () => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={Home} />
        <Route exact path='/register' component={Register} />
        <ProtectedRoute exact path='/organization' component={Organization} />
        <ProtectedRoute exact path='/organization/versions/:version' component={OrganizationVersion} />
        <ProtectedRoute exact path='/organization/reports' component={OrganizationReports} />
        <ProtectedRoute exact path='/programmes' component={Programmes} />
        <ProtectedRoute exact path='/programmes/:id' component={Programme} />
        <ProtectedRoute exact path='/programmes/:programmeId/versions/:version' component={ProgrammeVersion} />
        <ProtectedRoute exact path='/programmes/:programmeId/courses/:courseId' component={CourseProgrammePage} />
        <ProtectedRoute exact path='/programmes/:programmeId/courses/:courseId/reports' component={CourseProgrammeReports} />
        <ProtectedRoute exact path='/programmes/:programmeId/courses/:courseId/versions/:version' component={CourseProgrammeVersion} />
        <ProtectedRoute exact path='/programmes/:programmeId/reports' component={ProgrammeReports} />
        <ProtectedRoute exact path='/courses' component={Courses} />
        <ProtectedRoute exact path='/courses/:id' component={Course} />
        <ProtectedRoute exact path='/courses/:id/reports' component={CourseReports} />
        <ProtectedRoute exact path='/courses/:id/versions/:version' component={CourseVersion} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/work-assignments/' component={AllWorkAssignmentsPage} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/work-assignments/:workAssignmentId' component={WorkAssignment} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/work-assignments/:workAssignmentId/reports' component={WorkAssignmentReports} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/work-assignments/:workAssignmentId/versions/:version' component={WorkAssignmentVersion} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/exams/' component={AllExamsPage} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/exams/:examId' component={Exam} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/exams/:examId/reports' component={ExamReports} />
        <ProtectedRoute exact path='/courses/:courseId/terms/:termId/exams/:examId/versions/:version' component={ExamVersion} />
        <ProtectedRoute exact path='/classes' component={Classes} />
        <ProtectedRoute exact path='/classes/:classId' component={Class} />
        <ProtectedRoute exact path='/classes/:classId/reports' component={ClassReports} />
        <ProtectedRoute exact path='/classes/:classId/versions/:version' component={ClassVersion} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId' component={CourseClass} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/reports' component={CourseClassReports} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/lectures' component={AllLecturesPage} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/lectures/:lectureId' component={Lecture} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/lectures/:lectureId/reports' component={LectureReports} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/lectures/:lectureId/versions/:version' component={LectureVersion} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/homeworks' component={AllHomeworksPage} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/homeworks/:homeworkId' component={Homework} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/homeworks/:homeworkId/reports' component={HomeworkReports} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId/homeworks/:homeworkId/versions/:version' component={HomeworkVersion} />
        <ProtectedRoute exact path='/logout' component={Logout} />
        <ProtectedRoute exact path='/user' component={Profile} />
        <ProtectedRoute exact path='/users/:username' component={OtherUser} />
        <Redirect from='*' to='/' />
      </Switch>
    </BrowserRouter>
  </div>
)

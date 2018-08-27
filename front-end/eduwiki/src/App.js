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
import Home from './components/home/Home'
import Logout from './components/auth/Logout'
import CourseClass from './components/classes/CourseClass'
import ProtectedRoute from './components/auth/ProtectedRoute'
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom'

export default () => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={Home} />
        <Route exact path='/register' component={Register} />
        <ProtectedRoute exact path='/organization' component={Organization} />
        <ProtectedRoute exact path='/programmes' component={Programmes} />
        <ProtectedRoute exact path='/programmes/:id' component={Programme} />
        <ProtectedRoute exact path='/courses' component={Courses} />
        <ProtectedRoute exact path='/courses/:id' component={Course} />
        <ProtectedRoute exact path='/classes' component={Classes} />
        <ProtectedRoute exact path='/classes/:id' component={Class} />
        <ProtectedRoute exact path='/classes/:classId/courses/:courseId' component={CourseClass} />
        <ProtectedRoute exact path='/programmes/:programmeId/versions/:version' component={ProgrammeVersion} />
        <ProtectedRoute exact path='/organization/versions/:version' component={OrganizationVersion} />
        <ProtectedRoute exact path='/courses/:courseId/versions/:version' component={CourseVersion} />
        <ProtectedRoute exact path='/logout' component={Logout} />
        <ProtectedRoute exact path='/user' component={Profile} />
        <ProtectedRoute exact path='/users/:username' component={OtherUser} />
        <Redirect from='*' to='/' />
      </Switch>
    </BrowserRouter>
  </div>
)

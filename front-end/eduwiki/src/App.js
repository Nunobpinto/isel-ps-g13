import React from 'react'
import Profile from './components/user/Profile'
import OtherUser from './components/user/OtherUser'
import OrganizationVersion from './components/organization/OrganizationVersion'
import ProgrammeVersion from './components/programmes/ProgrammeVersion'
import CourseVersion from './components/courses/CourseVersion'
import Organization from './components/organization/Organization'
import Programmes from './components/programmes/Programmes'
import Programme from './components/programmes/Programme'
import Register from './components/auth/Register'
import Courses from './components/courses/Courses'
import Course from './components/courses/Course'
import Home from './components/Home/Home'
import Logout from './components/auth/Logout'
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom'

export default () => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={Home} />
        <Route exact path='/register' component={Register} />
        <Route exact path='/organization' render={props => <Organization {...props} />} />
        <Route exact path='/programmes' render={props => <Programmes {...props} />} />
        <Route exact path='/programmes/:id' render={props => <Programme {...props} />} />
        <Route exact path='/courses' render={props => <Courses {...props} />} />
        <Route exact path='/courses/:id' render={props => <Course {...props} />} />
        <Route exact path='/classes' render={props => <Courses {...props} />} />
        <Route exact path='/classes/:id' render={props => <Course {...props} />} />
        <Route exact path='/programmes/:programmeId/versions/:version' component={ProgrammeVersion} />
        <Route exact path='/organization/:id/versions/:version' component={OrganizationVersion} />
        <Route exact path='/courses/:courseId/versions/:version' component={CourseVersion} />
        <Route exact path='/logout' component={Logout} />
        <Route exact path='/user' component={Profile} />
        <Route exact path='/users/:username' component={OtherUser} />
        <Redirect from='*' to='/' />
      </Switch>
    </BrowserRouter>
  </div>
)

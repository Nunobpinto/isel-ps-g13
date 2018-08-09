import React from 'react'
import OrganizationVersion from './components/OrganizationVersion'
import ProgrammeVersion from './components/ProgrammeVersion'
import CourseVersion from './components/CourseVersion'
import Organization from './components/Organization'
import Programmes from './components/Programmes'
import Programme from './components/Programme'
import Register from './components/Register'
import Courses from './components/Courses'
import Course from './components/Course'
import Home from './components/Home'
import { BrowserRouter, Route, Switch } from 'react-router-dom'

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
      </Switch>
    </BrowserRouter>
  </div>
)

import React from 'react'
import Organization from './components/Organization'
import Programmes from './components/Programmes'
import Programme from './components/Programme'
import Courses from './components/Courses'
import Course from './components/Course'
import Home from './components/Home'
import { BrowserRouter, Route, Switch } from 'react-router-dom'

export default () => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={Home} />
        <Route exact path='/organization' render={props => <Organization {...props} />} />
        <Route exact path='/programmes' render={props => <Programmes {...props} />} />
        <Route exact path='/programmes/:id' render={props => <Programme {...props} />} />
        <Route exact path='/courses' render={props => <Courses {...props} />} />
        <Route exact path='/courses/:id' render={props => <Course {...props} />} />
      </Switch>
    </BrowserRouter>
    <p>This is a work in progress EduWiki version</p>
  </div>
)

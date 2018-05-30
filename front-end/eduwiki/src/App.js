import React from 'react'
import Organization from './components/Organization'
import Programme from './components/Programme'
import { BrowserRouter, Route, Switch } from 'react-router-dom'

export default () => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/organization' render={props => <Organization {...props} />} />
        <Route exact path='/programmes' render={props => <Programme {...props} />} />
        <Route exact path='/programmes/:programmeId' render={props => <Programme {...props} />} />
      </Switch>
    </BrowserRouter>
    <Programme />
  </div>
)

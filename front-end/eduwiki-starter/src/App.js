import React, { Component } from 'react'
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom'
import TenantForm from './components/TenanntForm'
import Home from './components/Home'

class App extends Component {
  render () {
    return (
      <BrowserRouter>
        <Switch>
          <Route exact path='/home' component={Home} />
          <Route exact path='/create-tenant' component={TenantForm} />
          <Redirect from='*' to='/home' />
        </Switch>
      </BrowserRouter>
    )
  }
}

export default App

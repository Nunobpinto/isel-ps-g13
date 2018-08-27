import React, { Component } from 'react'
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom'
import CreateTenant from './components/CreateTenant'
import Home from './components/Home'
import AdminLogin from './components/AdminLogin'
import AdminPage from './components/AdminPage'
import PendingTenant from './components/PendingTenant'
import ProtectedRoute from './components/ProtectedRoute'
import Logout from './components/Logout'

class App extends Component {
  render () {
    return (
      <BrowserRouter>
        <Switch>
          <Route exact path='/home' component={Home} />
          <Route exact path='/create-tenant' component={CreateTenant} />
          <Route exact path='/login' component={AdminLogin} />
          <ProtectedRoute exact path='/admin' component={AdminPage} />
          <ProtectedRoute exact path='/pending/:tennantId' component={PendingTenant} />
          <Redirect from='*' to='/home' />
        </Switch>
      </BrowserRouter>
    )
  }
}

export default App

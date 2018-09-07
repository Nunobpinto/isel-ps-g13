import React from 'react'
import {
  Route,
  Redirect,
  withRouter
} from 'react-router-dom'

const protectedRoute = ({ path, component: Component, ...props }) => (
  <Route
    {...props}
    render={compProps => {
      const auth = window.localStorage.getItem('auth')
      if (auth) {
        return (<Component {...compProps} />)
      }
      return (<Redirect to={{ pathname: '/', state: {from: props.location} }} />)
    }
    } />
)

export default withRouter(protectedRoute)

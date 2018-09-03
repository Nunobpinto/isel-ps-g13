import React from 'react'
import {
  Route,
  Redirect,
  withRouter
} from 'react-router-dom'

const privateRoute = ({ path, component: Component, ...props }) => (
  <Route
    {...props}
    render={compProps => {
      let cookie = window.localStorage.getItem('auth')
      if (cookie) {
        return (<Component {...compProps} />)
      }
      return (<Redirect to={{ pathname: '/', state: {from: props.location} }} />)
    }
    } />
)

export default withRouter(privateRoute)

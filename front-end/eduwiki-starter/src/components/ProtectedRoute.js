import React from 'react'
import {
  Route,
  Redirect,
  withRouter
} from 'react-router-dom'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const privateRoute = ({ path, component: Component, ...props }) => (
  <Route
    {...props}
    render={compProps => {
      let cookie = cookies.get('auth')
      if (cookie) {
        return (<Component {...compProps} />)
      }
      return (<Redirect to={{ pathname: '/', state: {from: props.location} }} />)
    }
    } />
)

export default withRouter(privateRoute)
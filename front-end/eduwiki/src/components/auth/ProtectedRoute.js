import React from 'react'
import {
  Route,
  Redirect
} from 'react-router-dom'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const checkAuth = () => cookies.get('auth')

export default ({ component: Component }) => (
  <Route render={props => (
    checkAuth() ? (
      <Component {...props} />
    ) : (
      <Redirect to={{ pathname: '/', state: {from: props.location} }} />
    )
  )} />
)

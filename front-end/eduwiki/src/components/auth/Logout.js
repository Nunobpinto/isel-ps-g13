import React from 'react'
import {Redirect} from 'react-router-dom'

export default (props) => {
  window.localStorage.removeItem('auth')
  return (
    <Redirect to='/' />
  )
}

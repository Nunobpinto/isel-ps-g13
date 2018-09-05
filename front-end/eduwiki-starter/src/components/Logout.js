import React from 'react'
import {Redirect} from 'react-router-dom'
import {message} from 'antd'

export default (props) => {
  window.localStorage.removeItem('auth')
  message.success('Logged out !!')
  return (
    <Redirect to='/' />
  )
}

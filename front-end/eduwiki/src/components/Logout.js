import React from 'react'
import { Redirect } from 'react-router-dom'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default () => {
  cookies.remove('auth')
  return (
    <Redirect to={{ pathname: '/login' }} />
  )
}

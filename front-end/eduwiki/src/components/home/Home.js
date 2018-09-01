import React from 'react'
import Login from '../auth/Login'
import Cookies from 'universal-cookie'
import UserPage from '../users/UserPage'
import Layout from '../layout/Layout'
const cookies = new Cookies()

export default (props, context) => {
  const authCookie = cookies.get('auth')
  if (authCookie) {
    return (
      <Layout>
        <UserPage auth={authCookie} history={props.history} />
      </Layout>
    )
  }
  return (
    <div>
      <div className='container'>
        <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
        <img alt='Isel Logo' id='isel-logo' src='isel_logo.jpg' />
        <Login history={props.history} destination={props.location.state} />
      </div>
    </div>
  )
}

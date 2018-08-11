import React from 'react'
import Login from '../auth/Login'
import { Link } from 'react-router-dom'
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
        <Login history={props.history} />
      </div>
      <h1>Current web pages</h1>
      <ul>
        <li>
          <Link to={{pathname: '/organization'}}>Organization</Link>
        </li>
        <li>
          <Link to={{pathname: '/programmes'}}>Programmes</Link>
        </li>
        <li>
          <Link to={{pathname: '/courses'}}>Courses</Link>
        </li>
        <li>
          <Link to={{pathname: '/classes'}}>Courses</Link>
        </li>
      </ul>
    </div>
  )
}

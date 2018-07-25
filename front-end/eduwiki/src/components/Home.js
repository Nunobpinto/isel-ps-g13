import React from 'react'
import Login from './Login'
import { Link } from 'react-router-dom'

export default (props) => {
  return (
    <div id='holder'>
      <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
      <div>
        <Login />
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
      </ul>
    </div>
  )
}

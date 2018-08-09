import React from 'react'
import fetch from 'isomorphic-fetch'
import MyLayout from '../layout/Layout'
import {Dropdown, Button, Tooltip, message} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      user: {
        username: '',
        familyName: '',
        givenName: '',
        personalEmail: '',
        organizationEmail: ''
      },
      programmeStages: [],
      programmeReports: []
    }
  }
  render () {
    return (
      <MyLayout>
        <div >
          <div className='left_side'>
            <img src='defaultUser.png' />
            <p><strong>Username</strong> : {this.state.user.username}</p>
            <p><strong>Name</strong> : {this.state.user.givenName} {this.state.user.familyName}</p>
            <p><strong>Personal email</strong> : {this.state.user.personalEmail}</p>
            <p><strong>Organization email</strong> : {this.state.user.organizationEmail}</p>
          </div>
          <div className='centre_div'>
            <h1>Ola</h1>
          </div>
          <div className='right_side'>
            <h1>Reputation</h1>
          </div>
        </div>
      </MyLayout>
    )
  }
  componentDidMount () {
    const auth = cookies.get('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth
      }
    }
    fetch('http://localhost:8080/user', options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error!!!')
        }
        return resp.json()
      })
      .then(json => this.setState({
        user: {
          username: json.username,
          familyName: json.familyName,
          givenName: json.givenName,
          personalEmail: json.personalEmail,
          organizationEmail: json.organizationEmail
        }

      }))
      .catch(_ => message.error('Something bad happened'))
  }
}

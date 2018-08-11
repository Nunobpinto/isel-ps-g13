
import React from 'react'
import fetch from 'isomorphic-fetch'
import MyLayout from '../layout/Layout'
import ProgrammesStage from '../programmes/ProgrammesStage'
import UserActivity from './UserActivity'
import {Layout, Menu, message} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()
const {Content} = Layout

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
      showProgrammeStage: true,
      showCourseStage: false
    }
  }
  render () {
    return (
      <MyLayout>
        <div >
          <div className='left_side'>
            <img src='defaultUser.png' />
          </div>
          <div className='centre_div'>
            <h1><strong>Username</strong> : {this.state.user.username}</h1>
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
    const username = this.props.match.params.username
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth
      }
    }
    fetch('http://localhost:8080/users/' + username, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error!!!')
        }
        return resp.json()
      })
      .then(user => {
        this.setState({
          user: {
            username: user.username
          }
        })
      })

      .catch(_ => message.error('Something bad happened'))
  }
}

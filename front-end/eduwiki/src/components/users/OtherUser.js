
import React from 'react'
import fetch from 'isomorphic-fetch'
import MyLayout from '../layout/Layout'
import ReportUser from './ReportUser'
import {message, Layout} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const { Content, Sider } = Layout

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
        <Layout style={{ padding: '24px 0', background: '#fff' }}>
          <Sider width={200} style={{ background: '#fff' }}>
            <img src='defaultUser.png' />
            <h1><strong>Username</strong> : {this.state.user.username}</h1>
          </Sider>
          <Content style={{ padding: '0 24px', minHeight: 280 }}>
            <ReportUser username={this.state.user.username} />
          </Content>
        </Layout>
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

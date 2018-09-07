import React from 'react'
import fetcher from '../../fetcher'
import MyLayout from '../layout/Layout'
import ReportUser from './ReportUser'
import {Layout, Button, Avatar, message} from 'antd'
import {Redirect} from 'react-router-dom'
import UserReports from './UserReports'
import config from '../../config'

const { Content, Sider } = Layout

class OtherUserDetail extends React.Component {
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
      <div>
        {this.props.user.username === this.props.match.params.username && <Redirect to='/user' />}
        <Layout style={{ padding: '24px 0', background: '#fff' }}>
          <Sider width={200} style={{ background: '#fff' }}>
            <Avatar src='/defaultUser.png' size='large' />
            <h1><strong>Username</strong> : {this.state.user.username}</h1>
          </Sider>
          <Content style={{ padding: '0 24px', minHeight: 280 }}>
            <Button onClick={() => this.setState({seeReports: true})}>Show User Reports</Button>
            { this.state.seeReports &&
              <div>
                <ReportUser username={this.state.user.username} />
                <UserReports
                  username={this.state.user.username}
                  authUser={this.props.user}
                />
              </div>}
          </Content>
        </Layout>
      </div>
    )
  }
  componentDidMount () {
    const auth = window.localStorage.getItem('auth')
    const username = this.props.match.params.username
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/users/' + username, options)
      .then(user => {
        this.setState({
          user: {
            username: user.username
          }
        })
      })
      .catch(error => message.error(error.detail))
  }
}

export default (props) => (
  <MyLayout>
    <OtherUserDetail match={props.match} />
  </MyLayout>
)


import React from 'react'
import fetcher from '../../fetcher'
import MyLayout from '../layout/Layout'
import ReportUser from './ReportUser'
import {message, Layout} from 'antd'
import {Redirect} from 'react-router-dom'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

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
            <img alt={'User Avatar'} src='defaultUser.png' />
            <h1><strong>Username</strong> : {this.state.user.username}</h1>
          </Sider>
          <Content style={{ padding: '0 24px', minHeight: 280 }}>
            <ReportUser username={this.state.user.username} />
          </Content>
        </Layout>
      </div>
    )
  }
  componentDidMount () {
    const auth = cookies.get('auth')
    const username = this.props.match.params.username
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/users/' + username, options)
      .then(user => {
        this.setState({
          user: {
            username: user.username
          }
        })
      })
      .catch(_ => message.error('Error fetching user'))
  }
}

export default (props) => (
  <MyLayout>
    <OtherUserDetail match={props.match} />
  </MyLayout>
)

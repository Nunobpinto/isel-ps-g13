import React from 'react'
import fetcher from '../../fetcher'
import MyLayout from '../layout/Layout'
import ProgrammesStage from '../programmes/ProgrammesStage'
import UserActivity from './UserActivity'
import UserReputation from './UserReputation'
import {Layout, Menu, message} from 'antd'
import Cookies from 'universal-cookie'
import CoursesStage from '../courses/CoursesStage'
const cookies = new Cookies()
const {Content} = Layout

export default (props) => (
  <MyLayout>
    <Profile />
  </MyLayout>
)
class Profile extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      user: {
        username: '',
        familyName: '',
        givenName: '',
        email: ''
      },
      showProgrammeStage: true,
      showCourseStage: false
    }
  }
  render () {
    return (
      <div >
        <div className='left_side'>
          <img alt={'User Avatar'} src='defaultUser.png' />
          <p>
            <h1><strong>Username</strong> : {this.state.user.username}</h1>
            <h1><strong>Name</strong> : {this.state.user.givenName} {this.state.user.familyName}</h1>
            <h1><strong>Email</strong> : {this.state.user.email}</h1>
            <h1>User Activity :</h1>
            <UserActivity />
          </p>
        </div>
        {
          this.props.user.reputation.role === 'ROLE_ADMIN'
            ? <div>
              <div className='centre_div'>

                <h1>Staged resources</h1>
                <Layout style={{ background: '#fff' }}>
                  <Menu
                    theme='light'
                    mode='horizontal'
                    defaultSelectedKeys={['1']}
                    defaultOpenKeys={['1']}
                  >
                    <Menu.Item
                      key={1}
                      onClick={() => this.setState({
                        showProgrammeStage: true,
                        showCourseStage: false
                      })}
                    >
                    Programmes
                    </Menu.Item>
                    <Menu.Item
                      key={2}
                      onClick={() => this.setState({
                        showProgrammeStage: false,
                        showCourseStage: true
                      })}
                    >
                    Courses
                    </Menu.Item>
                  </Menu>
                  <Content style={{ padding: '0 24px', minHeight: 280 }}>
                    {this.state.showProgrammeStage &&
                      <ProgrammesStage username={this.props.user.username} />
                    }
                    {this.state.showCourseStage &&
                      <CoursesStage username={this.props.user.username} />
                    }
                  </Content>
                </Layout>
              </div>
              <div className='right_side'>
                <h1>Reputation</h1>
                <UserReputation />
              </div>
            </div>
            : <div className='centre_div'>
              <h1>Reputation</h1>
              <UserReputation />
            </div>
        }
      </div>
    )
  }
  componentDidMount () {
    const auth = cookies.get('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user', options)
      .then(user => {
        this.setState({
          user: {
            username: user.username,
            familyName: user.familyName,
            givenName: user.givenName,
            email: user.email
          }
        })
      })
      .catch(_ => message.error('Error getting your profile'))
  }
}

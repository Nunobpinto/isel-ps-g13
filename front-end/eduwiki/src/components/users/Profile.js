import React from 'react'
import MyLayout from '../layout/Layout'
import ProgrammesStage from '../programmes/ProgrammesStage'
import UserActivity from './UserActivity'
import UserReputation from './UserReputation'
import {Layout, Menu} from 'antd'
import CoursesStage from '../courses/CoursesStage'
import ClassesStage from '../classes/ClassesStage'
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
      showCourseStage: false,
      showClassesStage: false
    }
  }
  render () {
    return (
      <div >
        <div className='left_side'>
          <img alt={'User Avatar'} src='defaultUser.png' />
          <p>
            <h1><strong>Username</strong> : {this.props.user.username}</h1>
            <h1><strong>Name</strong> : {this.props.user.givenName} {this.props.user.familyName}</h1>
            <h1><strong>Email</strong> : {this.props.user.email}</h1>
            <h1>User Activity :</h1>
            <UserActivity />
          </p>
        </div>
        {
          this.props.user.reputation.role === 'ROLE_ADMIN'
            ? <div>
              <div className='centre_div_profile'>

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
                        showCourseStage: false,
                        showClassesStage: false
                      })}
                    >
                    Programmes
                    </Menu.Item>
                    <Menu.Item
                      key={2}
                      onClick={() => this.setState({
                        showClassesStage: false,
                        showProgrammeStage: false,
                        showCourseStage: true
                      })}
                    >
                    Courses
                    </Menu.Item>
                    <Menu.Item
                      key={3}
                      onClick={() => this.setState({
                        showCourseStage: false,
                        showProgrammeStage: false,
                        showClassesStage: true
                      })}
                    >
                    Classes
                    </Menu.Item>
                  </Menu>
                  <Content style={{ padding: '0 24px', minHeight: 280 }}>
                    {this.state.showProgrammeStage &&
                      <ProgrammesStage username={this.props.user.username} />
                    }
                    {this.state.showCourseStage &&
                      <CoursesStage username={this.props.user.username} />
                    }
                    {this.state.showClassesStage &&
                      <ClassesStage username={this.props.user.username} />
                    }
                  </Content>
                </Layout>
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
}

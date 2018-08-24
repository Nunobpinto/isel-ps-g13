import React from 'react'
import fetcher from '../../fetcher'
import {Link} from 'react-router-dom'
import MyLayout from '../layout/Layout'
import CourseVersions from './CourseVersions'
import Term from '../term/Term'
import {Button, Card, Col, Row, Tooltip, Menu, Layout, Popover, message} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const { Content, Sider } = Layout
const { SubMenu } = Menu

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      version: 0,
      votes: 0,
      timestamp: '',
      terms: [],
      exams: [],
      classes: [],
      workAssignments: [],
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termId: undefined,
      courseError: undefined,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined,
      voteUp: false,
      voteDown: false,
      followCourseFlag: false,
      unFollowCourseFlag: false
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.showTerm = this.showTerm.bind(this)
    this.followCourse = this.followCourse.bind(this)
    this.unFollowCourse = this.unFollowCourse.bind(this)
  }

  showTerm (term) {
    this.setState({term: term})
  }

  render () {
    return (
      <div>
        <MyLayout>
          {this.state.courseError
            ? <p> Error getting this course, please try again !!! </p>
            : <div>
              <div className='title_div'>
                <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small></h1>
              </div>
              <div className='version_div'>
                <Popover placement='bottom' content={<CourseVersions auth={cookies.get('auth')} id={this.props.match.params.id} version={this.state.version} />} trigger='click'>
                  <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.version}
                  </Button>
                </Popover>
              </div>
              <div>
                <p>Created By : {this.state.createdBy}</p>
                <p>
                  Votes : {this.state.votes}
                  <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
                  </Tooltip>
                  <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
                  </Tooltip>
                  {this.state.userFollowing
                    ? <Tooltip placement='bottom' title={'UnFollow this Course'}>
                      <Button icon='close-circle' onClick={() => this.setState({unFollowCourseFlag: true})} />
                    </Tooltip>
                    : <Tooltip placement='bottom' title={'Follow this Course'}>
                      <Button icon='heart-o' onClick={() => this.setState({followCourseFlag: true})} />
                    </Tooltip>
                  }
                </p>
                {this.state.termError
                  ? <p>this.state.termError </p>
                  : <Layout style={{ padding: '24px 0', background: '#fff' }}>
                    <Sider width={200} style={{ background: '#fff' }}>
                      <Menu
                        mode='inline'
                        style={{ height: '100%' }}
                      >
                        {this.state.terms.map(item =>
                          <Menu.Item
                            key={item.termId}
                            onClick={() => this.showTerm(item)}
                          >
                            {item.shortName}
                          </Menu.Item>

                        )}
                      </Menu>
                    </Sider>
                    <Content style={{ padding: '0 24px', minHeight: 280 }}>
                      {this.state.term
                        ? <Term term={this.state.term} courseId={this.props.match.params.id} />
                        : <h1>Please choose one of the available Terms</h1>
                      }
                    </Content>
                  </Layout>
                }

              </div>
            </div>
          }
        </MyLayout>
      </div>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Vote Up!!')
        this.setState(prevState => ({
          voteUp: false,
          votes: prevState.votes + 1
        }))
      })
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Vote Down!!')
        this.setState(prevState => ({
          voteDown: false,
          votes: prevState.votes - 1
        }))
      })
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteDown: false})
      })
  }

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(course => {
        const termsUri = `http://localhost:8080/courses/${course.courseId}/terms`
        fetcher(termsUri, header)
          .then(terms => {
            const userCoursesUri = 'http://localhost:8080/user/courses'
            fetcher(userCoursesUri, header)
              .then(userCourses => this.setState({
                full_name: course.fullName,
                short_name: course.shortName,
                createdBy: course.createdBy,
                timestamp: course.timestamp,
                version: course.version,
                id: course.courseId,
                votes: course.votes,
                terms: terms.termList,
                userFollowing: (userCourses.courseList.find(crs => crs.courseId === course.courseId))
              }))
              .catch(_ => this.setState({
                full_name: course.fullName,
                short_name: course.shortName,
                createdBy: course.createdBy,
                timestamp: course.timestamp,
                version: course.version,
                id: course.courseId,
                votes: course.votes,
                terms: terms.termList
              }))
          })
          .catch(error => {
            message.error('Error fetching terms')
            this.setState({termError: error})
          })
      })
      .catch(error => {
        message.error('Error fetching course')
        this.setState({courseError: error})
      })
  }

  followCourse () {
    const data = {
      'course_id': this.state.courseId
    }
    const options = {
      method: 'POST',
      body: JSON.stringify(data),
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'

      }
    }
    fetcher('http://localhost:8080/user/courses', options)
      .then(_ => {
        message.success('Followed this course !!!')
        this.setState({
          followProgrammeFlag: false,
          userFollowing: true
        })
      })
      .catch(_ => {
        message.error('Error while following this course!!')
        this.setState({followProgrammeFlag: false})
      })
  }

  unFollowCourse () {
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user/courses/' + this.state.courseId, options)
      .then(_ => {
        message.success('Unfollowed this course !!!')
        this.setState({
          unFollowCourseFlag: false,
          userFollowing: false
        })
      })
      .catch(_ => {
        message.error('Error while unfollowing this course!!')
        this.setState({unFollowCourseFlag: false})
      })
  }

  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.followCourseFlag) {
      this.followCourse()
    } else if (this.state.unFollowCourseFlag) {
      this.unFollowCourse()
    }
  }
}

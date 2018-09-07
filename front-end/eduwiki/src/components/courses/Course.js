import React from 'react'
import fetcher from '../../fetcher'
import ReportCourse from './ReportCourse'
import MyLayout from '../layout/Layout'
import CourseVersions from './CourseVersions'
import TermMenu from '../term/TermMenu'
import {Button, Tooltip, Popover, message} from 'antd'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <MyLayout>
    <Course courseId={props.match.params.id} />
  </MyLayout>
)

class Course extends React.Component {
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
    this.followCourse = this.followCourse.bind(this)
    this.unFollowCourse = this.unFollowCourse.bind(this)
  }

  render () {
    return (
      <div>
        {this.state.courseError
          ? <p> Error getting this course, please try again !!! </p>
          : <div>
            <div className='title_div'>
              <h1>{this.state.full_name} - {this.state.short_name} <small>({timestampParser(this.state.timestamp)})</small></h1>
            </div>
            <div className='version_div'>
              <Popover placement='bottom' content={<CourseVersions auth={window.localStorage.getItem('auth')} id={this.props.courseId} version={this.state.version} />} trigger='click'>
                <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.version}
                </Button>
              </Popover>
            </div>
            <div>
              <p>Created By : <a href={`/users/${this.state.createdBy}`}>{this.state.createdBy}</a></p>
              <p>
                  Votes : {this.state.votes}
                <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
                  <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
                </Tooltip>
                <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                  <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
                </Tooltip>
                <Popover content={<ReportCourse id={this.props.courseId} />} trigger='click'>
                  <Tooltip placement='bottom' title='Report this Programme'>
                    <Button id='report_btn' shape='circle' icon='warning' />
                  </Tooltip>
                </Popover>
                <a href={`/courses/${this.props.courseId}/reports`}>
                  <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Course
                  </Button>
                </a>
                {this.state.userFollowing
                  ? <Tooltip placement='bottom' title={'UnFollow this Course'}>
                    <Button icon='close-circle' onClick={() => this.setState({unFollowCourseFlag: true})} />
                  </Tooltip>
                  : <Tooltip placement='bottom' title={'Follow this Course'}>
                    <Button icon='heart' onClick={() => this.setState({followCourseFlag: true})} />
                  </Tooltip>
                }
              </p>
              <TermMenu
                courseId={this.props.courseId}
                courseBeingFollowed={this.state.userFollowing}
                userRole={this.props.user.reputation.role}
              />
            </div>
          </div>
        }
      </div>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.courseId
    const uri = config.API_PATH + '/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
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
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const id = this.props.courseId
    const uri = config.API_PATH + '/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
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
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote down')
        }
        this.setState({voteDown: false})
      })
  }

  componentDidMount () {
    const id = this.props.courseId
    const uri = config.API_PATH + '/courses/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, header)
      .then(course => {
        const userCoursesUri = config.API_PATH + '/user/courses'
        fetcher(userCoursesUri, header)
          .then(userCourses => this.setState({
            full_name: course.fullName,
            short_name: course.shortName,
            createdBy: course.createdBy,
            timestamp: course.timestamp,
            version: course.version,
            id: course.courseId,
            votes: course.votes,
            userFollowing: (userCourses.courseList.find(crs => crs.courseId === course.courseId))
          }))
          .catch(_ => this.setState({
            full_name: course.fullName,
            short_name: course.shortName,
            createdBy: course.createdBy,
            timestamp: course.timestamp,
            version: course.version,
            id: course.courseId,
            votes: course.votes
          }))
      })
      .catch(error => {
        message.error('Error fetching terms')
        this.setState({termError: error})
      })
      .catch(error => {
        message.error('Error fetching course')
        this.setState({courseError: error})
      })
  }

  followCourse () {
    const data = {
      'course_id': this.props.courseId
    }
    const options = {
      method: 'POST',
      body: JSON.stringify(data),
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID

      }
    }
    fetcher(config.API_PATH + '/user/courses', options)
      .then(_ => {
        message.success('Followed this course !!!')
        this.setState({
          followCourseFlag: false,
          userFollowing: true
        })
      })
      .catch(_ => {
        message.error('Error while following this course!!')
        this.setState({followCourseFlag: false})
      })
  }

  unFollowCourse () {
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/user/courses/' + this.props.courseId, options)
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

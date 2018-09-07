import React from 'react'
import fetcher from '../../fetcher'
import {message, Tooltip, Button, Popover, Menu} from 'antd'
import Lectures from '../lectures/Lectures'
import Homeworks from '../homeworks/Homeworks'
import Layout from '../layout/Layout'
import ReportCourseClass from './ReportCourseClass'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <CourseClass classId={props.match.params.classId} courseId={props.match.params.courseId} />
  </Layout>
)

class CourseClass extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      flag: true,
      courseClass: undefined,
      userFollowing: false,
      canBeFollowed: false
    }
    this.getLectures = this.getLectures.bind(this)
    this.getHomeworks = this.getHomeworks.bind(this)
    this.fetchLectures = this.fetchLectures.bind(this)
    this.fetchHomeworks = this.fetchHomeworks.bind(this)
    this.followCourse = this.followCourse.bind(this)
    this.unFollowCourse = this.unFollowCourse.bind(this)
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }

  getLectures () {
    this.setState({
      showLectures: true,
      showHomeworks: false
    })
  }

  getHomeworks () {
    this.setState({
      showHomeworks: true,
      showLectures: false
    })
  }
  followCourse () {
    const inputModel = {
      course_class_id: this.state.courseClass.courseClassId,
      course_id: this.props.courseId
    }
    const url = config.API_PATH + '/user/classes'
    const options = {
      method: 'POST',
      body: JSON.stringify(inputModel),
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(_ => {
        message.success('Followed class')
        this.setState({
          canBeFollowed: false,
          userFollowing: true,
          followCourseFlag: false
        })
      })
      .catch(_ => {
        message.error('Error following class')
        this.setState({followCourseFlag: false})
      })
  }
  unFollowCourse () {
    const url = `${config.API_PATH}/user/classes/${this.state.courseClass.courseClassId}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(_ => {
        message.success('Unfollowed class')
        this.setState({
          canBeFollowed: true,
          userFollowing: false,
          unFollowCourseFlag: false
        })
      })
      .catch(_ => {
        message.error('Error unfollowing class')
        this.setState({unFollowCourseFlag: false})
      })
  }
  render () {
    return (
      <div>
        {this.state.courseClass
          ? <div>
            <div className='title_div'>
              <h1>
                <strong>Class {this.state.courseClass.lecturedTerm}</strong>
                    /
                <a href={`/classes/${this.props.classId}`}><strong>{this.state.courseClass.className}</strong></a>
                    /
                <a href={`/courses/${this.state.courseClass.courseId}`}><strong>{this.state.courseClass.courseShortName}</strong></a>
              </h1>
            </div>
            <h1>{this.state.courseClass.courseFullName} - <small>({timestampParser(this.state.courseClass.timestamp)})</small></h1>
            <p>Created By <a href={`/users/${this.state.courseClass.createdBy}`}>{this.state.courseClass.createdBy}</a></p>
            <p>
          Votes : {this.state.courseClass.votes}
              <Tooltip placement='bottom' title={`Vote Up`}>
                <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
              </Tooltip>
              <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
              </Tooltip>
              <Popover content={<ReportCourseClass classId={this.props.classId} courseId={this.props.courseId} termId={this.state.courseClass.termId} />} trigger='click'>
                <Tooltip placement='bottom' title='Report this Class'>
                  <Button id='report_btn' shape='circle' icon='warning' />
                </Tooltip>
              </Popover>
              <a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/reports`}>
                <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Course In Class
                </Button>
              </a>
              {this.state.canBeFollowed &&
              <Tooltip placement='bottom' title={'Follow this class'}>
                <Button id='report_btn' icon='heart' onClick={() => this.setState({followCourseFlag: true})} />
              </Tooltip>
              }
              {this.state.userFollowing &&
              <Tooltip placement='bottom' title={'Unfollow this class'}>
                <Button id='report_btn' icon='close-circle' onClick={() => this.setState({unFollowCourseFlag: true})} />
              </Tooltip>
              }
            </p>
            <Menu
              theme='light'
              mode='horizontal'
              defaultSelectedKeys={['1']}
              defaultOpenKeys={['1']}
            >
              <Menu.Item
                key={1}
                onClick={() => this.getLectures()}
              >
            Lectures
              </Menu.Item>
              <Menu.Item
                key={2}
                onClick={() => this.getHomeworks()}
              >
            Homeworks
              </Menu.Item>
            </Menu>
            {this.state.showLectures &&
              <Lectures lectures={this.state.lectures} classId={this.props.classId} courseId={this.props.courseId} />
            }
            {this.state.showHomeworks &&
              <Homeworks homeworks={this.state.homeworks} classId={this.props.classId} courseId={this.props.courseId} />
            }
          </div>
          : <h1>Can't get the Course you requested</h1>
        }
      </div>
    )
  }
  fetchLectures (classId, courseId) {
    const uri = `${config.API_PATH}/classes/${classId}/courses/${courseId}/lectures`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    return fetcher(uri, header)
  }

  fetchHomeworks (classId, courseId) {
    const uri = `${config.API_PATH}/classes/${classId}/courses/${courseId}/homeworks`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    return fetcher(uri, header)
  }
  componentDidUpdate () {
    if (this.state.followCourseFlag) {
      this.followCourse()
    } else if (this.state.unFollowCourseFlag) {
      this.unFollowCourse()
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const classId = this.props.classId
    const courseId = this.props.courseId
    const uri = `${config.API_PATH}/classes/${classId}/courses/${courseId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Up!!')
        this.setState(prevState => {
          let courseClass = prevState.courseClass
          courseClass.votes += 1
          return ({
            voteUp: false,
            courseClass: courseClass
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteUp: false })
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const classId = this.props.classId
    const courseId = this.props.courseId
    const uri = `${config.API_PATH}/classes/${classId}/courses/${courseId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Down!!')
        this.setState(prevState => {
          let courseClass = prevState.courseClass
          courseClass.votes -= 1
          return ({
            voteDown: false,
            courseClass: courseClass
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteDown: false })
      })
  }
  componentDidMount () {
    const classId = this.props.classId
    const courseId = this.props.courseId
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const uri = `${config.API_PATH}/classes/${classId}/courses/${courseId}`
    fetcher(uri, options)
      .then(courseClass => {
        this.fetchLectures(classId, courseId)
          .then(lectures => {
            this.fetchHomeworks(classId, courseId)
              .then(homeworks => {
                this.setState({
                  courseClass: courseClass,
                  lectures: lectures.lectureList,
                  homeworks: homeworks.homeworkList,
                  showLectures: true
                })
              })
              .catch(_ => {
                message.error('Error fetching homeworks')
                this.setState({
                  courseClass: courseClass,
                  lectures: lectures.lectureList,
                  showLectures: true
                })
              })
          })
          .catch(_ => {
            message.error('Error fetching lectures')
            this.fetchHomeworks(classId, courseId)
              .then(homeworks => {
                this.setState({
                  courseClass: courseClass,
                  homeworks: homeworks.homeworkList
                })
              })
              .catch(_ => {
                message.error('Error fetching lectures and homeworks')
                this.setState({courseClass: courseClass})
              })
          })
      })
      .catch(_ => message.error('Error fetching the course class'))
    const userClassesUrl = config.API_PATH + '/user/classes'
    fetcher(userClassesUrl, options)
      .then(json => {
        let userClasses = json.courseClassList
        const userFollowing = userClasses.find(cl => cl.classId === Number(this.props.classId))
        if (!userFollowing) throw new Error()
        this.setState({
          userFollowing: userFollowing
        })
      })
      .catch(_ => {
        const userCoursesUrl = config.API_PATH + '/user/courses'
        fetcher(userCoursesUrl, options)
          .then(json => {
            let userCourses = json.courseList
            if (userCourses.length === 0) throw new Error()
            this.setState(prevState => {
              const canBeFollowed = userCourses.find(cl => cl.courseId === Number(this.props.courseId))
              this.setState({
                canBeFollowed: canBeFollowed
              })
            })
          })
          .catch(_ => message.error('Please follow a course in order to follow one of its classes'))
      })
  }
}

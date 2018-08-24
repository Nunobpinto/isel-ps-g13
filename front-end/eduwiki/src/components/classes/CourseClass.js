import React from 'react'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
import {message, Tooltip, Button, Popover, Menu} from 'antd'
import Lectures from '../lectures/Lectures'
import Homeworks from '../homeworks/Homeworks'
import Layout from '../layout/Layout'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      flag: true,
      courseClass: undefined
    }
    this.getLectures = this.getLectures.bind(this)
    this.getHomeworks = this.getHomeworks.bind(this)
    this.fetchLectures = this.fetchLectures.bind(this)
    this.fetchHomeworks = this.fetchHomeworks.bind(this)
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
  render () {
    return (
      <Layout>
        {this.state.courseClass
          ? <div>
            <div className='title_div'>
              <h1>
                <strong>Class {this.state.courseClass.lecturedTerm}</strong>
                    /
                <strong>{this.state.courseClass.className}</strong>
                    /
                <strong>{this.state.courseClass.courseShortName}</strong>
              </h1>
            </div>
            <h1>{this.state.courseClass.courseFullName} - <small>({this.state.courseClass.timestamp})</small></h1>
            <p>Created By {this.state.courseClass.createdBy} </p>
            <p>
          Votes : {this.state.votes}
              <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
                <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
              </Tooltip>
              <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
              </Tooltip>
              <Popover content={<p>Report Class Course</p>} trigger='click'>
                <Tooltip placement='bottom' title='Report this Class'>
                  <Button id='report_btn' shape='circle' icon='warning' />
                </Tooltip>
              </Popover>
              <Popover placement='bottomLeft' content={<p>Show reports of Class Course</p>} trigger='click'>
                <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Programme
                </Button>
              </Popover>
              {this.state.canBeFollowed &&
              <Tooltip placement='bottom' title={'Follow this class'}>
                <Button id='report_btn' icon='heart' onClick={() => this.setState({followProgrammeFlag: true})} />
              </Tooltip>
              }
              {this.state.userFollowing &&
              <Tooltip placement='bottom' title={'Unfollow this class'}>
                <Button id='report_btn' icon='close-circle' onClick={() => this.setState({unFollowProgrammeFlag: true})} />
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
              <Lectures lectures={this.state.lectures} classId={this.props.match.params.classId} courseId={this.props.match.params.courseId} />
            }
            {this.state.showHomeworks &&
              <Homeworks homeworks={this.state.homeworks} classId={this.props.match.params.classId} courseId={this.props.match.params.courseId} />
            }
          </div>
          : <h1>Can't get the Course you requested</h1>
        }
      </Layout>
    )
  }
  fetchLectures (classId, courseId) {
    const uri = `http://localhost:8080/classes/${classId}/courses/${courseId}/lectures`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    return fetcher(uri, header)
  }

  fetchHomeworks (classId, courseId) {
    const uri = `http://localhost:8080/classes/${classId}/courses/${courseId}/homeworks`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    return fetcher(uri, header)
  }
  componentDidMount () {
    const classId = this.props.match.params.classId
    const courseId = this.props.match.params.courseId
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const uri = `http://localhost:8080/classes/${classId}/courses/${courseId}`
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
  }
}

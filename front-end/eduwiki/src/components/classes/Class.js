import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import ReportClass from './ReportClass'
import ClassVersions from './ClassVersions'
import Layout from '../layout/Layout'
import { Button, Row, Col, message, Tooltip, Card, Popover } from 'antd'
import Cookies from 'universal-cookie'
import CoursesClassStage from '../courseClass/CoursesClassStage'
const cookies = new Cookies()

export default (props) => (
  <Layout>
    <Class match={props.match} history={props.history} />
  </Layout>
)

class Class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      klass: {},
      courses: [],
      stagedCourses: [],
      otherCourses: []
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.addCourseToClass = this.addCourseToClass.bind(this)
    this.addStagedCourseToClas = this.addStagedCourseToClass.bind(this)
    this.fetchOtherCourses = this.fetchOtherCourses.bind(this)
    this.approveStagedCourseInClass = this.approveStagedCourseInClass.bind(this)
  }
  render () {
    return (
      <div>
        <div className='title_div'>
          <h1>
            <strong>Class {this.state.klass.lecturedTerm}</strong>
             /
            <strong>{this.state.klass.programmeShortName}</strong>
             /
            <strong>{this.state.klass.className} - ({this.state.klass.timestamp})</strong>
          </h1>
        </div>
        <div className='version_div'>
          <Popover placement='bottom' content={<ClassVersions auth={cookies.get('auth')} classId={this.props.match.params.classId} version={this.state.klass.version} />} trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.klass.version}
            </Button>
          </Popover>
        </div>
        <div>
          <p>Created By : {this.state.klass.createdBy}</p>
          <p>
                  Votes : {this.state.klass.votes}
            <Tooltip placement='bottom' title={`Vote Up on ${this.state.klass.className}`}>
              <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
            </Tooltip>
            <Tooltip placement='bottom' title={`Vote Down on ${this.state.klass.className}`}>
              <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
            </Tooltip>
            <Popover content={<ReportClass classId={this.props.match.params.classId} programmeId={this.state.klass.programmeId} />} trigger='click'>
              <Tooltip placement='bottom' title='Report this Class'>
                <Button id='report_btn' shape='circle' icon='warning' />
              </Tooltip>
            </Popover>
            <a href={`/classes/${this.props.match.params.classId}/reports`}>
              <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Class
              </Button>
            </a>
          </p>
        </div>
        <div style={{ padding: '20px' }}>
          <h1>Courses</h1>
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              {this.state.courses.map(item =>
                <Col span={8} key={item.courseId}>
                  <Card title={item.courseShortName}>
                    <p>Created by {item.createdBy}</p>
                    <p>Votes : {item.votes}</p>
                    <p>Added at {item.timestamp}</p>
                    <Link to={{pathname: `/classes/${item.classId}/courses/${item.courseId}`}}>See it's page</Link>
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        </div>
        <div>
          <div style={{ padding: '30px' }}>
            <CoursesClassStage
              classId={this.props.match.params.classId}
              isAdmin={this.props.user.reputation.role === 'ROLE_ADMIN'}
              staged={this.state.staged}
              adminAction={(stagedId) => this.setState({
                adminAction: true,
                stagedId: stagedId
              })}
            />
            <Button onClick={() => this.setState({seeAllCourses: true})}>Add Course To This Class</Button>
            <p>Courses that aren't already associated with {this.state.klass.className}</p>
            <Row gutter={16}>
              {this.state.otherCourses.map(crs =>
                <Col span={8} key={crs.courseId}>
                  <Card title={crs.shortName}>
                    <p>{crs.fullName} ({crs.shortName}) - <small> Created By {crs.createdBy}</small> </p>
                    <Button
                      type='primary'
                      onClick={() => this.setState({
                        addCourse: true,
                        course: crs
                      })}
                    >
                          Add To Class
                    </Button>
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        </div>
      </div>
    )
  }
  approveStagedCourseInClass (stageId) {
    const id = this.props.match.params.classId
    const uri = 'http://localhost:8080/classes/' + id + '/courses/stage/' + stageId
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, body)
      .then(courseProgramme => {
        message.success('Approved Course!!')
        this.setState(prevState => {
          let courses = prevState.courses
          courses.push(courseProgramme)
          let otherCourses = prevState.otherCourses
          const idx = otherCourses.findIndex(crs => crs.courseId === courseProgramme.courseId)
          delete otherCourses[idx]
          return ({
            adminAction: false,
            courses: courses,
            otherCourses: otherCourses
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while approving this course programme')
        }
        this.setState({ adminAction: false, approved: false })
      })
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/vote`
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
    fetcher(url, body)
      .then(_ => {
        message.success('Voted up!!!')
        this.setState(prevState => {
          prevState.klass.votes += 1
          return ({
            klass: prevState.klass,
            voteUp: false
          })
        })
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
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/vote`
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
    fetcher(url, body)
      .then(_ => {
        message.success('Voted up!!!')
        this.setState(prevState => {
          prevState.klass.votes -= 1
          return ({
            klass: prevState.klass,
            voteDown: false
          })
        })
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
  addCourseToClass () {
    const id = this.props.match.params.classId
    const courseToAdd = this.state.course
    if (this.props.user.reputation.role !== 'ROLE_ADMIN') {
      return this.addStagedCourseToClass()
    }
    const url = 'http://localhost:8080/classes/' + id + '/courses/' + courseToAdd.courseId
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(courseProgramme => {
        this.setState(prevState => {
          let courses = prevState.courses
          courses.push(courseProgramme)
          let otherCourses = prevState.otherCourses
          const index = otherCourses.findIndex(course => course.courseId === courseProgramme.courseId)
          otherCourses.splice(index, 1)
          message.success('Added course to class with success')
          return ({
            addCourse: false,
            courses: courses,
            otherCourses: otherCourses
          })
        })
      })
      .catch(_ => {
        message.error('Error while adding course to class')
        this.setState({addCourse: false})
      })
  }

  addStagedCourseToClass () {
    const id = this.props.match.params.classId
    const courseToAdd = this.state.course
    const url = 'http://localhost:8080/classes/' + id + '/courses/' + courseToAdd.courseId + '/stage'
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(courseProgramme => {
        message.success('Added to stage courses')
        this.setState({
          addCourse: false,
          staged: courseProgramme
        })
      })
      .catch(_ => {
        message.error('Error while adding course to class')
        this.setState({addCourse: false})
      })
  }
  fetchOtherCourses () {
    const uri = 'http://localhost:8080/courses'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(json => this.setState(prevState => {
        let classCourses = prevState.courses
        let newCourses = json.courseList.filter(crs => !classCourses.some(cr => crs.courseId === cr.courseId))
        return ({
          seeAllCourses: false,
          otherCourses: newCourses
        })
      }))
      .catch(_ => message.error('Error fetching other courses'))
  }
  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.seeAllCourses) {
      this.fetchOtherCourses()
    } else if (this.state.addCourse) {
      this.addCourseToClass()
    } else if (this.state.adminAction) {
      this.approveStagedCourseInClass(this.state.stagedId)
    }
  }
  componentDidMount () {
    const id = this.props.match.params.classId
    const uri = 'http://localhost:8080/classes/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(klass => {
        const coursesUri = `http://localhost:8080/classes/${id}/courses`
        fetcher(coursesUri, header)
          .then(json => {
            this.setState({
              klass: klass,
              courses: json.courseClassList
            })
          })
          .catch(_ => {
            message.error('Error fetching courses from class')
            this.setState({klass: klass})
          })
      })
      .catch(error => {
        message.error('Error fetching class with id ' + id)
        this.setState({error: error})
      })
  }
}

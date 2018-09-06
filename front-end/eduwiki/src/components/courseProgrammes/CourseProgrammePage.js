import React from 'react'
import {Link} from 'react-router-dom'
import {Col, Card, Button, Popover, Tooltip, message} from 'antd'
import ReportCourseProgramme from './ReportCourseProgramme'
import CourseProgrammeVersions from './CourseProgrammeVersions'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <CourseProgramme programmeId={props.match.params.programmeId} courseId={props.match.params.courseId} />
  </Layout>
)

class CourseProgramme extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      course: {}
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }
  render () {
    return (
      <div>
        <h1>Course <a href={`/courses/${this.props.courseId}`}>{this.state.course.shortName}</a> on <a href={`/programmes/${this.props.programmeId}`}>{this.state.course.programmeShortName}</a></h1>
        <Col span={8} key={this.state.course.courseId}>
          <Card title={this.state.course.shortName}>
            <p>{this.state.course.fullName} - <small>{timestampParser(this.state.course.timestamp)}</small> </p>
            {this.state.course.optional === false ? <p>Mandatory</p> : <p>Optional</p>}
            <p>Credits : {this.state.course.credits}</p>
            <p>Lectured in term {this.state.course.lecturedTerm} </p>
            <p>Created By: <a href={`/users/${this.state.course.createdBy}`}>{this.state.course.createdBy}</a></p>
            <Popover placement='bottom' content={<CourseProgrammeVersions courseId={this.state.course.courseId} programmeId={this.props.programmeId} version={this.state.course.version} />} trigger='click'>
              <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.course.version}
              </Button>
            </Popover>
            <Popover content={<ReportCourseProgramme programmeId={this.props.programmeId} courseId={this.state.course.courseId} />} trigger='click'>
              <Tooltip placement='bottom' title='Report this Course Programme'>
                <Button id='report_btn' shape='circle' icon='warning' />
              </Tooltip>
            </Popover>
          Votes : {this.state.course.votes}
            <Tooltip placement='bottom' title={`Vote Up on ${this.state.course.shortName}`}>
              <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
            </Tooltip>
            <Tooltip placement='bottom' title={`Vote Down on ${this.state.course.shortName}`}>
              <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
            </Tooltip>
            <a href={`/programmes/${this.props.programmeId}/courses/${this.props.courseId}/reports`}><Button>Show All Reports</Button></a>
            <Link to={{pathname: `/courses/${this.props.courseId}`}}>See it's page</Link>
          </Card>
        </Col>
      </div>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.programmeId
    const uri = config.API_PATH + '/programmes/' + id + '/courses/' + this.state.course.courseId + '/vote'
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
          let course = prevState.course
          course.votes += 1
          return ({
            voteUp: false,
            course: course
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
    const id = this.props.programmeId
    const uri = config.API_PATH + '/programmes/' + id + '/courses/' + this.state.course.courseId + '/vote'
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
          let course = prevState.course
          course.votes -= 1
          return ({
            voteDown: false,
            course: course
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
  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
  componentDidMount () {
    const url = `${config.API_PATH}/programmes/${this.props.programmeId}/courses/${this.props.courseId}`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(course => this.setState({course: course}))
      .catch(_ => message.error('Error fetching this course programme'))
  }
}

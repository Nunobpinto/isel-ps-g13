import React from 'react'
import {Card, message, Breadcrumb, Popover, Button, Tooltip, Row, Col} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import LectureVersions from './LectureVersions'
import ReportLecture from './ReportLecture'
import timestampParser from '../../timestampParser'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lecture: {}
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.parseDuration = this.parseDuration.bind(this)
    this.parseTime = this.parseTime.bind(this)
  }
  render () {
    const lecture = this.state.lecture
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{lecture.lecturedTerm}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{lecture.className}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{lecture.courseShortName}</strong></Breadcrumb.Item>
          </Breadcrumb>
          <h1>{lecture.weekDay} - {this.parseTime(lecture.begins)} added in {timestampParser(lecture.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <Popover
            placement='bottom'
            content={
              <LectureVersions
                auth={window.localStorage.getItem('auth')}
                courseId={this.props.match.params.courseId}
                classId={this.props.match.params.classId}
                lectureId={this.props.match.params.lectureId}
                version={lecture.version}
              />
            } trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {lecture.version}
            </Button>
          </Popover>
        </div>
        <p>Created By : <a href={`/users/${lecture.createdBy}`}>{lecture.createdBy}</a></p>
        <p>
          Votes : {lecture.votes}
          <Tooltip placement='bottom' title={`Vote Up`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title='Report this Lecture'>
            <Button id='report_btn' shape='circle' icon='warning' onClick={() => this.setState({report: true})} />
          </Tooltip>
          <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push(`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}/reports`)}>
              Show all Reports On This Lecture
          </Button>
        </p>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Duration'>
                {this.parseDuration(lecture.duration)}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Location'>
                {lecture.location}
              </Card>
            </Col>
          </Row>
          {this.state.report &&
          <ReportLecture
            courseId={this.props.match.params.courseId}
            classId={this.props.match.params.classId}
            lectureId={this.props.match.params.lectureId}
          />
          }
        </div>
        <a href={`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}`}>
          <Button>Go Back To {lecture.className}/{lecture.courseShortName}</Button>
        </a>
      </Layout>
    )
  }
  parseDuration (duration) {
    if (duration) {
      return duration.slice(2)
    }
  }
  parseTime (time) {
    if (time) {
      const splitted = time.split(':')
      return `${splitted[0]}:${splitted[1]}`
    }
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}/vote`
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
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        prevState.lecture.votes += 1
        message.success('Successfully voted!!')
        return ({
          voteUp: false,
          lecture: prevState.lecture
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}/vote`
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
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        prevState.lecture.votes -= 1
        message.success('Successfully voted!!')
        return ({
          voteDown: false,
          lecture: prevState.lecture
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
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
    const uri = `http://localhost:8080/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(lecture => this.setState({lecture: lecture}))
      .catch(_ => message.error('Error getting the Specific Lecture'))
  }
}

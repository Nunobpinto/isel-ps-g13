import React from 'react'
import {Button, Row, Col, Card, message, Breadcrumb, Popover, Tooltip} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import ReportExam from './ReportExam'
import ExamVersions from './ExamVersions'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      exam: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `${config.API_PATH}/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    const exam = this.state.exam
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{exam.termShortName}</strong></Breadcrumb.Item>
            <Breadcrumb.Item>
              <a href={`/courses/${exam.courseId}`}><strong>{exam.courseShortName}</strong></a>
            </Breadcrumb.Item>
          </Breadcrumb>
          <h1>{exam.type} - {exam.phase} added in {timestampParser(exam.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <Popover
            placement='bottom'
            content={
              <ExamVersions
                auth={window.localStorage.getItem('auth')}
                courseId={this.props.match.params.courseId}
                termId={this.props.match.params.termId}
                examId={this.props.match.params.examId}
                version={exam.version} />
            } trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {exam.version}
            </Button>
          </Popover>
        </div>
        <p>Created By : <a href={`/users/${exam.createdBy}`}>{exam.createdBy}</a></p>
        <p>
          Votes : {exam.votes}
          <Tooltip placement='bottom' title={`Vote Up`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title='Report this Exam'>
            <Button id='report_btn' shape='circle' icon='warning' onClick={() => this.setState({report: true})} />
          </Tooltip>
          <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push(`/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}/reports`)}>
              Show all Reports On This Exam
          </Button>
        </p>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Due Date'>
                {exam.dueDate}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Location'>
                {exam.location}
              </Card>
            </Col>
            {exam.sheetId &&
            <Col span={5}>
              <Card title='Sheet'>
                <button onClick={() => this.showResource(exam.sheetId)}> See exam sheet</button>
              </Card>
            </Col>
            }
          </Row>
          {this.state.report &&
          <ReportExam
            courseId={this.props.match.params.courseId}
            termId={this.props.match.params.termId}
            examId={this.props.match.params.examId} />
          }
        </div>
      </Layout>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}/vote`
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
        prevState.exam.votes += 1
        message.success('Successfully voted!!')
        return ({
          voteUp: false,
          exam: prevState.exam
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
        return ({
          voteUp: false
        })
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const url = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}/vote`
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
        prevState.exam.votes -= 1
        message.success('Successfully voted!!')
        return ({
          voteDown: false,
          exam: prevState.exam
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
        return ({
          voteDown: false
        })
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
    const uri = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(exam => this.setState({exam: exam}))
      .catch(_ => message.error('Error getting the Specific Exam'))
  }
}

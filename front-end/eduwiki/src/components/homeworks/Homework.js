import React from 'react'
import {Button, Row, Col, Card, message, Breadcrumb, Tooltip, Popover} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import HomeworkVersions from './HomeworkVersions'
import ReportHomework from './ReportHomework'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      homework: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `${config.API_PATH}/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    const homework = this.state.homework
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{homework.lecturedTerm}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{homework.className}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{homework.courseShortName}</strong></Breadcrumb.Item>
          </Breadcrumb>
          <h1>{homework.homeworkName} added in {timestampParser(homework.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <Popover
            placement='bottom'
            content={
              <HomeworkVersions
                auth={window.localStorage.getItem('auth')}
                courseId={this.props.match.params.courseId}
                classId={this.props.match.params.classId}
                homeworkId={this.props.match.params.homeworkId}
                version={homework.version}
              />
            } trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {homework.version}
            </Button>
          </Popover>
        </div>
        <p>Created By : <a href={`/users/${homework.createdBy}`}>{homework.createdBy}</a></p>
        <p>
          Votes : {homework.votes}
          <Tooltip placement='bottom' title={`Vote Up`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title='Report this Homework'>
            <Button id='report_btn' shape='circle' icon='warning' onClick={() => this.setState({report: true})} />
          </Tooltip>
          <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push(`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}/reports`)}>
              Show all Reports On This Homework
          </Button>
        </p>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Due Date'>
                {homework.dueDate}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Multiple Deliveries'>
                {homework.multipleDeliveries ? 'Yes' : 'No'}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Late Delivery'>
                {homework.lateDelivery ? 'Yes' : 'No'}
              </Card>
            </Col>
            {homework.sheetId &&
            <Col span={5}>
              <Card title='Sheet'>
                <button onClick={() => this.showResource(homework.sheetId)}> See Homework sheet</button>
              </Card>
            </Col>
            }
          </Row>
          {this.state.report &&
          <ReportHomework
            courseId={this.props.match.params.courseId}
            classId={this.props.match.params.classId}
            homeworkId={this.props.match.params.homeworkId}
          />
          }
        </div>
        <a href={`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}`}><Button>Go Back to Course Class</Button></a>
      </Layout>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `${config.API_PATH}/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}/vote`
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
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        prevState.homework.votes += 1
        message.success('Successfully voted!!')
        return ({
          voteUp: false,
          homework: prevState.homework
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
        this.setState({
          voteUp: false
        })
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const url = `${config.API_PATH}/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}/vote`
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
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        prevState.homework.votes -= 1
        message.success('Successfully voted!!')
        return ({
          voteDown: false,
          homework: prevState.homework
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error processing your vote!!')
        }
        this.setState({voteDown: false})
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
    const uri = `${config.API_PATH}/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, header)
      .then(homework => this.setState({homework: homework}))
      .catch(error => message.error(error.detail))
  }
}

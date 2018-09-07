import React from 'react'
import {Button, Row, Col, Card, message, Breadcrumb, Popover, Tooltip} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import WorkAssignmentVersions from './WorkAssignmentVersions'
import ReportWorkAssignment from './ReportWorkAssignment'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      work: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `${config.API_PATH}/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    const work = this.state.work
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{work.termShortName}</strong></Breadcrumb.Item>
            <Breadcrumb.Item>
              <a href={`/courses/${this.props.match.params.courseId}`}><strong>{work.courseShortName}</strong></a>
            </Breadcrumb.Item>
          </Breadcrumb>
          <h1>Work Assignment {work.phase} added in {timestampParser(work.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <Popover
            placement='bottom'
            content={
              <WorkAssignmentVersions
                auth={window.localStorage.getItem('auth')}
                courseId={this.props.match.params.courseId}
                termId={this.props.match.params.termId}
                workAssignmentId={this.props.match.params.workAssignmentId}
                version={work.version} />
            } trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {work.version}
            </Button>
          </Popover>
        </div>
        <p>Created By : <a href={`/users/${work.createdBy}`}>{work.createdBy}</a></p>
        <p>
          Votes : {work.votes}
          <Tooltip placement='bottom' title={`Vote Up`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title='Report this Work Assignment'>
            <Button id='report_btn' shape='circle' icon='warning' onClick={() => this.setState({report: true})} />
          </Tooltip>
          <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push(`/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}/reports`)}>
              Show all Reports On This Work Assignment
          </Button>
        </p>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Due Date'>
                {work.dueDate}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Individual'>
                {work.individual ? 'Yes' : 'No'}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Late Delivery'>
                {work.lateDelivery ? 'Yes' : 'No'}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Multiple Deliveries'>
                {work.multipleDeliveries ? 'Yes' : 'No'}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Requires Report'>
                {work.requiresReport ? 'Yes' : 'No'}
              </Card>
            </Col>
            {work.sheetId &&
            <Col span={5}>
              <Card title='Sheet'>
                <Button onClick={() => this.showResource(work.sheetId)}> See assignment sheet</Button>
              </Card>
            </Col>
            }
            {work.supplementId &&
            <Col span={5}>
              <Card title='Supplement'>
                <Button onClick={() => this.showResource(work.supplementId)}> See assignment supplement</Button>
              </Card>
            </Col>
            }
          </Row>
          {this.state.report &&
            <ReportWorkAssignment
              courseId={this.props.match.params.courseId}
              termId={this.props.match.params.termId}
              workAssignmentId={this.props.match.params.workAssignmentId} />
          }
        </div>
      </Layout>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}/vote`
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
        prevState.work.votes += 1
        message.success('Successfully voted!!')
        return ({
          voteUp: false,
          work: prevState.work
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
    const url = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}/vote`
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
        prevState.work.votes -= 1
        message.success('Successfully voted!!')
        return ({
          voteDown: false,
          work: prevState.work
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
    const uri = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, header)
      .then(work => this.setState({work: work}))
      .catch(error => message.error(error.detail))
  }
}

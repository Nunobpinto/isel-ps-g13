import React from 'react'
import {Button, Row, Col, Card, message, Breadcrumb, Icon} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
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
            <Breadcrumb.Item><strong>{work.courseShortName}</strong></Breadcrumb.Item>
          </Breadcrumb>
          <h1>Work Assignment {work.phase} added in {timestampParser(work.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <h1>
              Version {work.version}
          </h1>
        </div>
        <p>Created By : <a href={`/users/${work.createdBy}`}>{work.createdBy}</a></p>
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
          <Button type='primary' onClick={() => this.props.history.push(`/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}`)}>
            <Icon type='left' />Back to actual version
          </Button>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `${config.API_PATH}/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}/versions/${this.props.match.params.version}`
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

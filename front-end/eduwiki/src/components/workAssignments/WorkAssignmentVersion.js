import React from 'react'
import Cookies from 'universal-cookie'
import {Button, Row, Col, Card, message, Breadcrumb, Icon} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      work: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
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
          <h1>Work Assignment {work.phase} added in {work.timestamp}</h1>
        </div>
        <div className='version_div'>
          <h1>
              Version {work.version}
          </h1>
        </div>
        <p>Created By : {work.createdBy}</p>
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
    const uri = `http://localhost:8080/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/work-assignments/${this.props.match.params.workAssignmentId}/versions/${this.props.match.params.version}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(work => this.setState({work: work}))
      .catch(_ => message.error('Error getting the Specific Work Assignment'))
  }
}

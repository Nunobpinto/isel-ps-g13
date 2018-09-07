import React from 'react'
import {Row, Col, Card, message, Breadcrumb, Button, Icon} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
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
          <h1>Version {homework.version}</h1>
        </div>
        <p>Created By : <a href={`/users/${homework.createdBy}`}>{homework.createdBy}</a></p>
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
          <Button type='primary' onClick={() => this.props.history.push(`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}`)}>
            <Icon type='left' />Back to actual version
          </Button>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `${config.API_PATH}/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/homeworks/${this.props.match.params.homeworkId}/versions/${this.props.match.params.version}`
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

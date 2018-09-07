import React from 'react'
import Layout from '../layout/Layout'
import fetcher from '../../fetcher'
import {Row, Col, Card, Button, Icon, message} from 'antd'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programmeId: 0,
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      timestamp: '',
      versionNumber: ''
    }
  }
  render () {
    return (
      <Layout>
        <div className='title_div'>
          <h1>{this.state.course} on {this.state.programme}<small>{timestampParser(this.state.timestamp)}</small></h1>
        </div>
        <div className='version_div'>
          <p>Version {this.state.versionNumber}</p>
        </div>
        <p>Created By : <a href={`/users/${this.state.createdBy}`}>{this.state.createdBy}</a></p>
        <div style={{ padding: '20px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Lectured Term'>
                {this.state.lecturedTerm}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Credits'>
                {this.state.credits}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Optional'>
                {this.state.optional ? 'Yes' : 'No'}
              </Card>
            </Col>
          </Row>
        </div>
        <Button type='primary' onClick={() => this.props.history.push(`/programmes/${this.props.match.params.programmeId}`)}>
          <Icon type='left' />Back to actual version
        </Button>
      </Layout>
    )
  }
  componentDidMount () {
    const programmeId = this.props.match.params.programmeId
    const courseId = this.props.match.params.courseId
    const versionNumber = this.props.match.params.version
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    const uri = `${config.API_PATH}/programmes/${programmeId}/courses/${courseId}/versions/${versionNumber}`
    fetcher(uri, options)
      .then(json => this.setState({
        lecturedTerm: json.lecturedTerm,
        optional: json.optional,
        credits: json.credits,
        programmeId: json.programmeId,
        courseId: json.courseId,
        versionNumber: json.version,
        createdBy: json.createdBy,
        timestamp: json.timestamp,
        programme: json.programmeShortName,
        course: json.courseShortName
      }))
      .catch(_ => message.error('Error fetching programme version'))
  }
}

import React from 'react'
import {Row, Col, Card, message, Breadcrumb, Button, Icon} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import timestampParser from '../../timestampParser'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lecture: {}
    }
    this.parseDuration = this.parseDuration.bind(this)
    this.parseTime = this.parseTime.bind(this)
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
  render () {
    const lecture = this.state.lecture
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{lecture.lecturedTerm}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><a><strong>{lecture.className}</strong></a></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{lecture.courseShortName}</strong></Breadcrumb.Item>
          </Breadcrumb>
          <h1>{lecture.weekDay} - {this.parseTime(lecture.begins)} added in {timestampParser(lecture.timestamp)}</h1>
        </div>
        <div className='version_div'>
          <h1>
              Version {lecture.version}
          </h1>
        </div>
        <p>Created By : <a href={`/users/${lecture.createdBy}`}>{lecture.createdBy}</a></p>
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
          <Button type='primary' onClick={() => this.props.history.push(`/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}`)}>
            <Icon type='left' />Back to actual version
          </Button>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/classes/${this.props.match.params.classId}/courses/${this.props.match.params.courseId}/lectures/${this.props.match.params.lectureId}/versions/${this.props.match.params.version}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(lecture => this.setState({lecture: lecture}))
      .catch(_ => message.error('Error getting the Specific Version of lecture'))
  }
}

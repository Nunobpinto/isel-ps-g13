import React from 'react'
import {Row, Col, Card, message, Breadcrumb, Button, Icon} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      exam: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    const exam = this.state.exam
    return (
      <Layout>
        <div className='title_div'>
          <Breadcrumb>
            <Breadcrumb.Item><strong>{exam.termShortName}</strong></Breadcrumb.Item>
            <Breadcrumb.Item><strong>{exam.courseShortName}</strong></Breadcrumb.Item>
          </Breadcrumb>
          <h1>{exam.type} - {exam.phase} added in {exam.timestamp}</h1>
        </div>
        <div className='version_div'>
          <p> Version {exam.version}</p>
        </div>
        <p>Created By : {exam.createdBy}</p>
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
          <Button type='primary' onClick={() => this.props.history.push(`/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}`)}>
            <Icon type='left' />Back to actual version
          </Button>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/courses/${this.props.match.params.courseId}/terms/${this.props.match.params.termId}/exams/${this.props.match.params.examId}/versions/${this.props.match.params.version}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(exam => this.setState({exam: exam}))
      .catch(_ => message.error('Error getting the Specific Version Of Exam'))
  }
}

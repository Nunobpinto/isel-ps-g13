import React from 'react'
import Cookies from 'universal-cookie'
import {Button, Row, Col, Card, message} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
const cookies = new Cookies()

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
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={8} key={exam.examId}>
              <Card
                title={`${exam.type} - ${exam.phase} - ${exam.dueDate} - ${exam.votes} Votes`}
                actions={[
                  <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: exam.examId})} />,
                  <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: exam.examId})} />
                ]}>
                <p>Location : {exam.location}</p>
                <p>Created By : {exam.createdBy}</p>
                <p>Added in : {exam.timestamp}</p>
                <button onClick={() => this.showExamsheet(exam.sheetId)}> See exam sheet</button>
              </Card>
            </Col>
          </Row>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${this.props.examId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(exam => this.setState({exam: exam}))
      .catch(_ => message.error('Error getting the Specific Exam'))
  }
}

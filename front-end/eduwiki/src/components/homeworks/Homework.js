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
      homework: {}
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    const homework = this.state.homework
    return (
      <Layout>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={8} key={homework.homeworkId}>
              <Card
                title={`${homework.homeworkName} ${homework.votes} Votes`}
                actions={[
                  <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: homework.examId})} />,
                  <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: homework.examId})} />
                ]}
              >
                <p>Late Delivery : {homework.lateDelivery ? 'Yes' : 'No'}</p>
                <p>Multiple Deliveries : {homework.multipleDeliveries ? 'Yes' : 'No'}</p>
                <p>Added in : {homework.timestamp}</p>
                <p>Created By : {homework.createdBy}</p>
                <button onClick={() => this.showResource(homework.sheetId)}> See Homework sheet</button>
              </Card>
            </Col>
          </Row>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/${this.props.homeworkId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(homework => this.setState({homework: homework}))
      .catch(_ => message.error('Error getting the Specific Homework'))
  }
}

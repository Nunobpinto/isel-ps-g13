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
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            <Col span={8} key={work.workAssignmentId}>
              <Card
                title={`${work.phase} - ${work.dueDate} - ${work.votes} Votes`}
                actions={[
                  <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: work.examId})} />,
                  <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: work.examId})} />
                ]}
              >
                <p>Individual : {work.individual ? 'Yes' : 'No'}</p>
                <p>Late Delivery : {work.lateDelivery ? 'Yes' : 'No'}</p>
                <p>Multiple Deliveries : {work.multipleDeliveries ? 'Yes' : 'No'}</p>
                <p>Requires Report : {work.requiresReport ? 'Yes' : 'No'}</p>
                <p>Added in : {work.timestamp}</p>
                <p>Created By : {work.createdBy}</p>
                <button onClick={() => this.showResource(work.sheetId)}> See Work Assignment sheet</button>
                {work.supplementId && <button onClick={() => this.showResource(work.supplementId)}> See Supplement</button>}
              </Card>
            </Col>
          </Row>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/${this.props.workAssignmentId}`
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

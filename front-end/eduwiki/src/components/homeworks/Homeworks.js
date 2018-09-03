import React from 'react'
import {Col, Row, Button, Card} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      homeworks: props.homeworks
    }
    this.showResource = this.showResource.bind(this)
  }
  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
    window.open(resourceUrl)
  }
  render () {
    return (
      <div>
        {this.state.homeworks.map(work => (
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <Col span={8} key={work.homeworkId}>
                <Card title={`${work.homeworkName} ${work.votes} Votes`}>
                  <p>Late Delivery : {work.lateDelivery ? 'Yes' : 'No'}</p>
                  <p>Multiple Deliveries : {work.multipleDeliveries ? 'Yes' : 'No'}</p>
                  <p>Added in : {work.timestamp}</p>
                  <p>Created By : {work.createdBy}</p>
                  <button onClick={() => this.showResource(work.sheetId)}> See Homework sheet</button>
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks`}><Button>See Staged or Add a new Homework</Button></a>
      </div>
    )
  }
}

import React from 'react'
import Cookies from 'universal-cookie'
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
                <Card
                  title={`${work.votes} Votes`}
                  actions={[
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: work.examId})} />,
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: work.examId})} />
                  ]}
                >
                  <p>Late Delivery : {work.lateDelivery}</p>
                  <p>Multiple Deliveries : {work.multipleDeliveries}</p>
                  <p>Added in : {work.timestamp}</p>
                  <p>Created By : {work.createdBy}</p>
                  <button onClick={() => this.showResource(work.sheetId)}> See Homework sheet</button>
                </Card>
              </Col>
            </Row>
          </div>
        ))}
      </div>
    )
  }
}

import React from 'react'
import {Row, Col, Card, Button} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lectures: props.lectures
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
    const splitted = time.split(':')
    return `${splitted[0]}:${splitted[1]}`
  }
  render () {
    return (
      <div>
        {this.state.lectures.map(item => (
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <Col span={8} key={item.lectureId}>
                <Card title={item.weekDay}>
                  <p>Created By : {item.createdBy}</p>
                  <p>Begins : {this.parseTime(item.begins)}</p>
                  <p>Duration : {this.parseDuration(item.duration)}</p>
                  <p>Location : {item.location}</p>
                  <p>Timestamp: {item.timestamp}</p>
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/lectures`}><Button>See Staged or Add a new Lecture</Button></a>
      </div>
    )
  }
}

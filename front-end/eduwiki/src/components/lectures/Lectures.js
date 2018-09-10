import React from 'react'
import {Row, Col, Card, Button} from 'antd'
import timestampParser from '../../timestampParser'

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
                  <p>Created By : <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                  <p>Begins : {this.parseTime(item.begins)}</p>
                  <p>Duration : {this.parseDuration(item.duration)}</p>
                  <p>Location : {item.location}</p>
                  <p>Created At : {timestampParser(item.timestamp)}</p>
                  <a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/${item.lectureId}`}><p>See its page</p></a>
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

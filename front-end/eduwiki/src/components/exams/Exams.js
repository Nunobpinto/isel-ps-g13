import React from 'react'
import {Card, List, Button} from 'antd'
import timestampParser from '../../timestampParser'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      up: false,
      down: false,
      dueDate: undefined,
      type: '',
      phase: '',
      location: ''
    }
  }
  render () {
    return (
      <div id={`exms_term_${this.props.termId}`}>
        <List
          grid={{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }}
          dataSource={this.props.exams}
          renderItem={exam => (
            <List.Item>
              <Card title={`${exam.type} - ${exam.phase} - ${exam.dueDate} - ${exam.votes} Votes`} >
                <p>Created at: {timestampParser(exam.timestamp)}</p>
                <a href={`/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${exam.examId}`}>See it's page</a>
              </Card>
            </List.Item>
          )}
        />
        <a href={`/courses/${this.props.courseId}/terms/${this.props.termId}/exams`}><Button>See Staged Or Create a new exam</Button></a>
      </div>
    )
  }
}

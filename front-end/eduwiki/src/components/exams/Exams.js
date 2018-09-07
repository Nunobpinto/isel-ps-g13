import React from 'react'
import {Card, List, Button} from 'antd'

export default (props) => (
  <div>
    <List
      dataSource={props.exams}
      renderItem={exam => (
        <List.Item>
          <Card title={`${exam.type} - ${exam.votes} Votes`} >
            <p>Phase: {exam.phase}</p>
            <a href={`/courses/${props.courseId}/terms/${props.termId}/exams/${exam.examId}`}>See it's page</a>
          </Card>
        </List.Item>
      )}
    />
    <a href={`/courses/${props.courseId}/terms/${props.termId}/exams`}><Button>See Staged Or Create a new exam</Button></a>
  </div>
)

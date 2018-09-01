import React from 'react'
import {Card, Button, List} from 'antd'
export default (props) => (
  <div>
    <List
      grid={{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }}
      dataSource={props.works}
      renderItem={work => (
        <List.Item>
          <Card title={`${work.phase} - ${work.votes} Votes`} >
            <a href={`/courses/${props.courseId}/terms/${props.termId}/work-assignments/${work.workAssignmentId}`}>See it's page</a>
          </Card>
        </List.Item>
      )}
    />
    <a href={`/courses/${props.courseId}/terms/${props.termId}/work-assignments`}><Button>See Staged Or Create a new Work Assignment</Button></a>
  </div>
)

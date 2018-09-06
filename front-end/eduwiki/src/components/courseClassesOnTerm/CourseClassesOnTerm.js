import React from 'react'
import {Card, List} from 'antd'

export default (props) => (
  <div style={{ padding: '30px' }}>
    <List
      grid={{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }}
      dataSource={props.classes}
      renderItem={klass => (
        <List.Item>
          <Card title={`${klass.className} - ${klass.votes} votes`} >
            <a href={`/classes/${klass.classId}/courses/${props.courseId}`}>See it's page</a>
          </Card>
        </List.Item>
      )}
    />
  </div>
)

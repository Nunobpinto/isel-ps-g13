import React from 'react'
import {List, Card, Button} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lectures: props.lectures
    }
  }
  render () {
    return (
      <div>
        <List id='staged-list'
          grid={{ gutter: 20, column: 2 }}
          dataSource={this.state.lectures}
          renderItem={item => (
            <List.Item>
              <Card title={item.weekDay}>
                <p>Created By : {item.createdBy}</p>
                <p>Begins : {item.begins}</p>
                <p>Duration : {item.duration}</p>
                <p>Location : {item.location}</p>
                <p>Timestamp: {item.timestamp}</p>
              </Card>
            </List.Item>
          )}
        />
        <a><Button>See staged or Add a new Lecture</Button></a>
      </div>
    )
  }
}

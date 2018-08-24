import React from 'react'
import Cookies from 'universal-cookie'
import {List, Card} from 'antd'
import IconText from '../comms/IconText'
const cookies = new Cookies()

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
              <IconText
                type='like-o'
                id='like_btn'
                onClick={() =>
                  this.setState({
                    voteUpStaged: true,
                    stageID: item.stagedId
                  })}
                text={item.votes}
              />
              <IconText
                type='dislike-o'
                id='dislike_btn'
                onClick={() =>
                  this.setState({
                    voteDownStaged: true,
                    stageID: item.stagedId
                  })}
              />
            </List.Item>
          )}
        />
      </div>
    )
  }
}

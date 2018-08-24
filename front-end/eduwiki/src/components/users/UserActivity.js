import React from 'react'
import { Timeline, message } from 'antd'
import Cookies from 'universal-cookie'
import fetcher from '../../fetcher'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      actions: []
    }
  }
  render () {
    return (
      <Timeline>
        {this.state.actions.map(action => (
          <Timeline.Item>
            {action.action_type} {action.entity_type} on {action.timestamp}
          </Timeline.Item>
        ))}
      </Timeline>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/user/actions`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, options)
      .then(json => this.setState({actions: json.actions}))
      .catch(error => {
        console.log(error)
        message.error('Error getting your past actions')
      })
  }
}

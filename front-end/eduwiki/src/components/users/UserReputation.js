import React from 'react'
import { List, message } from 'antd'
import Cookies from 'universal-cookie'
import fetcher from '../../fetcher'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reputationChanges: []
    }
  }
  render () {
    return (
      <List
        bordered
        dataSource={this.state.reputationChanges}
        renderItem={item => (
          <List.Item>
            <p>
              {item.pointsGiven > 0 ? '+' : '-' } {item.pointsGiven} given
              by {item.givenBy} for {item.action.action_type} {item.action.entity_type} on {item.action.timestamp}
            </p>
          </List.Item>
        )}
      />
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/user/reputation`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, options)
      .then(json => {
        this.setState({reputationChanges: json.reputationChanges})
      })
      .catch(error => {
        console.log(error)
        message.error('Error getting your reputation history')
      })
  }
}

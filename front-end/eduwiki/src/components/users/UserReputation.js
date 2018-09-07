import React from 'react'
import { List, message } from 'antd'
import fetcher from '../../fetcher'
import timestampParser from '../../timestampParser'
import config from '../../config';

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
              by {item.givenBy} for {item.action.action_type} {item.action.entity_type} on {timestampParser(item.action.timestamp)}
            </p>
          </List.Item>
        )}
      />
    )
  }
  componentDidMount () {
    const uri = `${config.API_PATH}/user/reputation`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
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

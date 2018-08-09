import React from 'react'
import fetch from 'isomorphic-fetch'
import {List} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      versions: []
    }
  }
  render () {
    return (
      <List
        itemLayout='vertical'
        bordered
        dataSource={this.state.versions}
        renderItem={item => (
          <List.Item>
            <a href={`/programmes/${this.props.id}/versions/${item.version}`}>
                    version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const programmeId = this.props.id
    const versionNumber = this.props.version
    const url = 'http://localhost:8080/programmes/' + programmeId + '/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth
      }
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error !!!')
        }
        return resp.json()
      })
      .then(versions => {
        const programmeVersions = versions.filter(version => version.version !== versionNumber)
        this.setState({versions: programmeVersions})
      })
  }
}

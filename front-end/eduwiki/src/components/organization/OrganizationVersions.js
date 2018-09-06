import React from 'react'
import {List, message} from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'

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
            <a href={`/organization/versions/${item.version}`}>
                      version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const url = config.API_PATH + '/organization/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        const versionNumber = this.props.version
        const versionsArray = versions.organizationVersionList
        const organizationVersions = versionsArray.filter(version => version.version !== versionNumber)
        this.setState({versions: organizationVersions})
      })
      .catch(error => message.error(error.detail))
  }
}

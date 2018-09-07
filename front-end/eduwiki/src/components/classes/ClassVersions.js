import React from 'react'
import fetcher from '../../fetcher'
import {List, message} from 'antd'
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
            <a href={`/classes/${this.props.classId}/versions/${item.version}`}>
                    version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const classId = this.props.classId
    const versionNumber = this.props.version
    const url = `${config.API_PATH}/classes/${classId}/versions`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, body)
      .then(versions => {
        let classVersions = versions.classVersionList
        classVersions = classVersions.filter(version => version.version !== versionNumber)
        this.setState({versions: classVersions})
      })
      .catch(_ => message.error('Error fetching class versions'))
  }
}

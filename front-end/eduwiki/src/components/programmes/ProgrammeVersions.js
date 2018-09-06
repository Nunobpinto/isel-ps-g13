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
    const url = config.API_PATH + '/programmes/' + programmeId + '/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        const programmeVersions = versions.programmeVersionList.filter(version => version.version !== versionNumber)
        this.setState({versions: programmeVersions})
      })
      .catch(error => message.error(error.detail))
  }
}

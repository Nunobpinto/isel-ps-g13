import React from 'react'
import fetcher from '../../fetcher'
import {List, message} from 'antd'

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
    const url = `http://localhost:8080/classes/${classId}/versions`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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


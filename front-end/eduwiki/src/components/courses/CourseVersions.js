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
            <a href={`/courses/${this.props.id}/versions/${item.version}`}>
                      version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const courseId = this.props.id
    const url = 'http://localhost:8080/courses/' + courseId + '/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        const courseVersions = versions.courseVersionList.filter(version => version.version !== this.props.version)
        this.setState({versions: courseVersions})
      })
      .catch(_ => message.error('Error getting all Versions of Course'))
  }
}

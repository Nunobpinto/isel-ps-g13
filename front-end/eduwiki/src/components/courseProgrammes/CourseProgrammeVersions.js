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
            <a href={`/programmes/${this.props.programmeId}/courses/${this.props.courseId}/versions/${item.version}`}>
                    version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const programmeId = this.props.programmeId
    const courseId = this.props.courseId
    const versionNumber = this.props.version
    const url = 'http://localhost:8080/programmes/' + programmeId + '/courses/' + courseId + '/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        let programmeVersions = versions.courseProgrammeVersionList
        programmeVersions = programmeVersions.filter(version => version.version !== versionNumber)
        this.setState({versions: programmeVersions})
      })
      .catch(_ => message.error('Error fetching programme versions'))
  }
}


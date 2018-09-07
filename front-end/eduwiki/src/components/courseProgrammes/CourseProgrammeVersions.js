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
    const url = config.API_PATH + '/programmes/' + programmeId + '/courses/' + courseId + '/versions'
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
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

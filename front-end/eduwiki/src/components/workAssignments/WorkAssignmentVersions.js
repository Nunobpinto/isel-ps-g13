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
            <a href={`/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/${this.props.workAssignmentId}/versions/${item.version}`}>
                      version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/${this.props.workAssignmentId}/versions`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        const workVersions = versions.workAssignmentVersionList.filter(version => version.version !== this.props.version)
        this.setState({versions: workVersions})
      })
      .catch(_ => message.error('Error getting all Versions of Work Assignment'))
  }
}

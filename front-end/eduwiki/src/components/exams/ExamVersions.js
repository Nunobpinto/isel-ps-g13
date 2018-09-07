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
            <a href={`/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${this.props.examId}/versions/${item.version}`}>
                      version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${this.props.examId}/versions`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, body)
      .then(versions => {
        const examVersions = versions.examVersionList.filter(version => version.version !== this.props.version)
        this.setState({versions: examVersions})
      })
      .catch(_ => message.error('Error getting all Versions of Exam'))
  }
}

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
            <a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/${this.props.homeworkId}/versions/${item.version}`}>
                      version {item.version}
            </a>
          </List.Item>)}
      />
    )
  }
  componentDidMount () {
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/${this.props.homeworkId}/versions`
    const body = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, body)
      .then(versions => {
        const homeworkVersions = versions.homeworkVersionList.filter(version => version.version !== this.props.version)
        this.setState({versions: homeworkVersions})
      })
      .catch(_ => message.error('Error getting all Versions of Homework'))
  }
}

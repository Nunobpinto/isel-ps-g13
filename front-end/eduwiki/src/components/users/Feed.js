import React from 'react'
import { List, Avatar, message } from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'
import timestampParser from '../../timestampParser'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      loadingFeed: true,
      actions: [{
        action_type: '',
        entity_type: '',
        entity_link: '',
        timestamp: ''
      }]
    }
    this.parseActionType = this.parseActionType.bind(this)
    this.parseEntityType = this.parseEntityType.bind(this)
  }
  render () {
    return (
      <List
        loading={this.state.loadingFeed}
        itemLayout='horizontal'
        dataSource={this.state.actions}
        renderItem={item => (
          <List.Item
          >
            <List.Item.Meta
              avatar={<Avatar src='defaultUser.png' />}
              title={`${item.action_user} ${this.parseActionType(item.action_type)} ${this.parseEntityType(item.entity_type)}`}
              description={timestampParser(item.timestamp)}
            />
            {
              !this.parseEntityType(item.entity_type).includes('staged') &&
              !this.parseEntityType(item.entity_type).includes('report') &&
              <a href={`/${item.entity_link}`}>See it's page</a>
            }
          </List.Item>
        )}
      />
    )
  }
  parseActionType (action) {
    switch (action) {
      case 'CREATE': return 'created - '
      case 'ALTER': return 'changed - '
      case 'DELETE': return 'deleted - '
      case 'VOTE_UP': return 'voted up on - '
      case 'VOTE_DOWN': return 'voted down on - '
      case 'APPROVE_REPORT': return 'approved - '
      case 'APPROVE_STAGE': return 'approved - '
      case 'REJECT_REPORT': return 'rejected - '
      case 'REJECT_STAGE': return 'rejected - '
      default: return ''
    }
  }
  parseEntityType (entity) {
    switch (entity) {
      case 'class' : return 'class'
      case 'class_report' : return 'report on class'
      case 'class_stage' : return 'staged class'
      case 'course': return 'course'
      case 'course_report': return 'report on course'
      case 'course_stage': return 'staged course'
      case 'course_class' : return 'course in class'
      case 'course_class_report' : return 'report on course in class'
      case 'course_class_stage' : return 'staged course in class'
      case 'course_programme': return 'course in programme'
      case 'course_programme_report': return 'course in programme report'
      case 'course_programme_stage': return 'course in programme stage'
      case 'exam' : return 'exam'
      case 'exam_report' : return 'report on exam'
      case 'exam_stage' : return 'staged course in class'
      case 'homework' : return 'homework'
      case 'homework_report' : return 'report on homework'
      case 'homework_stage' : return 'staged homework'
      case 'lecture' : return 'lecture'
      case 'lecture_report' : return 'report on lecture'
      case 'lecture_stage' : return 'staged lecture'
      case 'organization' : return 'organization'
      case 'organization_report' : return 'report on organization'
      case 'programme' : return 'programme'
      case 'programme_report' : return 'report on programme'
      case 'programme_stage' : return 'staged programme'
      case 'work_assignment' : return 'work assignment'
      case 'work_assignment_report' : return 'report on work assignment'
      case 'work_assignment_stage' : return 'staged work assignment'
      default: return ''
    }
  }
  componentDidMount () {
    const uri = `${config.API_PATH}/user/feed`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, options)
      .then(json => this.setState({
        actions: json.actions,
        loadingFeed: false
      }))
      .catch(error => {
        console.log(error)
        message.error('Error getting your past actions')
        this.setState({
          loadingFeed: false
        })
      })
  }
}

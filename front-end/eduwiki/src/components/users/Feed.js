import React from 'react'
import { List, Avatar, message } from 'antd'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

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
              description={item.timestamp}
            />
            {
              !this.parseEntityType(item.action_type).includes('staged') &&
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
      case 'CREATE': return 'created a'
      case 'ALTER': return 'changed a'
      case 'DELETE': return 'deleted a'
      case 'VOTE_UP': return 'voted up on a'
      case 'VOTE_DOWN': return 'voted down on a'
      case 'APPROVE_REPORT': return 'approved a'
      case 'APPROVE_STAGE': return 'approved a'
      case 'REJECT_REPORT': return 'rejected a'
      case 'REJECT_STAGE': return 'rejected a'
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
      case 'work_assignment' : return 'work_assignment'
      case 'work_assignment_report' : return 'report on work_assignment'
      case 'work_assignment_stage' : return 'staged work_assignment'
      default: return ''
    }
  }
  componentDidMount () {
    const uri = `http://localhost:8080/user/feed`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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

import React from 'react'
import { Timeline, message } from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      actions: []
    }
    this.parseActionType = this.parseActionType.bind(this)
  }
  render () {
    return (
      <Timeline>
        {this.state.actions.map(action => (
          <Timeline.Item>
            {this.parseActionType(action.action_type)} {this.parseEntityType(action.entity_type)} on {action.timestamp}
          </Timeline.Item>
        ))}
      </Timeline>
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
    const uri = `${config.API_PATH}/user/actions`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, options)
      .then(json => this.setState({actions: json.actions}))
      .catch(error => {
        message.error(error.detail)
      })
  }
}

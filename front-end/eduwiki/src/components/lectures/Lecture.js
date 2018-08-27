import React from 'react'
import Cookies from 'universal-cookie'
import {Card, message} from 'antd'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import IconText from '../comms/IconText'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lecture: {}
    }
  }
  render () {
    const lecture = this.state.lecture
    return (
      <Layout>
        <div style={{ padding: '30px' }}>
          <Card title={lecture.weekDay}>
            <p>Created By : {lecture.createdBy}</p>
            <p>Begins : {lecture.begins}</p>
            <p>Duration : {lecture.duration}</p>
            <p>Location : {lecture.location}</p>
            <p>Timestamp: {lecture.timestamp}</p>
          </Card>
          <IconText
            type='like-o'
            id='like_btn'
            onClick={() =>
              this.setState({
                voteUpStaged: true,
                stageID: lecture.stagedId
              })}
            text={lecture.votes}
          />
          <IconText
            type='dislike-o'
            id='dislike_btn'
            onClick={() =>
              this.setState({
                voteDownStaged: true,
                stageID: lecture.stagedId
              })}
          />
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/${this.props.lectureId}`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(lecture => this.setState({lecture: lecture}))
      .catch(_ => message.error('Error getting the Specific Lecture'))
  }
}

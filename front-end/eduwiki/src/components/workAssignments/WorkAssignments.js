import React from 'react'
import fetch from 'isomorphic-fetch'
import {Card, Col, Row, Button} from 'antd'
import Cookies from 'universal-cookie'
import SubmitWorkAssignment from './SubmitWorkAssignment'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      up: false,
      down: false,
      works: props.works,
      courseId: props.courseId,
      termId: props.termId,
      sheet: undefined,
      supplement: undefined,
      dueDate: undefined,
      individual: false,
      phase: '',
      lateDelivery: false,
      multipleDeliveries: false,
      requiresReport: false
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.showResource = this.showResource.bind(this)
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const examId = this.state.id
    const url = `http://localhost:8080/courses/${this.state.courseId}/terms/${this.state.termId}/exams/${this.state.id}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.exams]
          const index = newArray.findIndex(exam => exam.examId === examId)
          newArray[index].votes = prevState.exams[index].votes + 1
          return ({
            exams: newArray,
            up: false
          })
        })
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const examId = this.state.id
    const url = `http://localhost:8080/courses/${this.state.courseId}/terms/${this.state.termId}/exams/${this.state.id}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.exams]
          const index = newArray.findIndex(exam => exam.examId === examId)
          newArray[index].votes = prevState.exams[index].votes - 1
          return ({
            exams: newArray,
            down: false
          })
        })
      })
  }

  componentWillReceiveProps (props) {
    this.setState({
      works: props.works,
      courseId: props.courseId,
      termId: props.termId
    })
  }

  render () {
    return (
      <div>
        {this.state.works.map(work => (
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <Col span={8} key={work.examId}>
                <Card
                  title={`${work.phase} - ${work.dueDate} - ${work.votes} Votes`}
                  actions={[
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: work.examId})} />,
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: work.examId})} />
                  ]}
                >
                  <p>Individual : {work.individual}</p>
                  <p>Late Delivery : {work.lateDelivery}</p>
                  <p>Multiple Deliveries : {work.multipleDeliveries}</p>
                  <p>Requires Report : {work.requiresReport}</p>
                  <p>Added in : {work.timestamp}</p>
                  <p>Created By : {work.createdBy}</p>
                  <button onClick={() => this.showResource(work.sheetId)}> See Work Assignment sheet</button>
                  <button onClick={() => this.showResource(work.supplementId)}> See Supplement</button>
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <Button onClick={() => this.setState({createWork: true})}>Add Work Assignment</Button>
        {this.state.createWork &&
          <SubmitWorkAssignment
            courseId={this.props.courseId}
            termId={this.props.termId}
          />
        }
      </div>
    )
  }

  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
    window.open(resourceUrl)
  }

  componentDidUpdate () {
    if (this.state.up) {
      this.voteUp()
    } else if (this.state.down) {
      this.voteDown()
    }
  }
}

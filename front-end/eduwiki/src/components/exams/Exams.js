import React from 'react'
import fetch from 'isomorphic-fetch'
import {Card, Col, Row, Button} from 'antd'
import Cookies from 'universal-cookie'
import SubmitExam from './SubmitExam'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      up: false,
      down: false,
      exams: props.exams,
      courseId: props.courseId,
      termId: props.termId,
      dueDate: undefined,
      type: '',
      phase: '',
      location: ''
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.showExamsheet = this.showExamsheet.bind(this)
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
      exams: props.exams,
      courseId: props.courseId,
      termId: props.termId
    })
  }

  render () {
    return (
      <div id={`exms_term_${this.props.termId}`}>
        {this.state.exams.map(exam => (
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <Col span={8} key={exam.examId}>
                <Card
                  title={`${exam.type} - ${exam.phase} - ${exam.dueDate} - ${exam.votes} Votes`}
                  actions={[
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: exam.examId})} />,
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: exam.examId})} />
                  ]}
                >
                  <p>Location : {exam.location}</p>
                  <p>Created By : {exam.createdBy}</p>
                  <p>Added in : {exam.timestamp}</p>
                  <button onClick={() => this.showExamsheet(exam.sheetId)}> See exam sheet</button>
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <Button onClick={() => this.setState({createExam: true})}>Add exam</Button>
        {this.state.createExam &&
          <SubmitExam
            courseId={this.props.courseId}
            termId={this.props.termId}
          />
        }
      </div>
    )
  }

  showExamsheet (sheet) {
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

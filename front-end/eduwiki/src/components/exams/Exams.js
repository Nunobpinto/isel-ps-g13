import React from 'react'
import fetch from 'isomorphic-fetch'
import {Card, Col, Row, Upload, Button, Icon, message} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const uploadProps = {
  name: 'file',
  action: '//jsonplaceholder.typicode.com/posts/',
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'Authorization': 'Basic ' + cookies.get('auth')
  },
  onChange(info) {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  },
}

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      up: false,
      down: false,
      exams: props.exams,
      courseId: props.courseId,
      termId: props.termId
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const examId = this.state.id
    const url = `http://localhost:8080/courses/${this.state.courseId}/terms/${this.state.termId}/exams/${this.state.id}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
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
      vote: 'Down',
      created_by: 'ze'
    }
    const examId = this.state.id
    const url = `http://localhost:8080/courses/${this.state.courseId}/terms/${this.state.termId}/exams/${this.state.id}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
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
                  title={`${exam.type} - ${exam.phase} - ${exam.dueDate} - ${exam.votes}`}
                  actions={[
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({up: true, id: exam.examId})} />,
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({down: true, id: exam.examId})} />
                  ]}
                >
                  {exam.sheet}
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <Upload {...uploadProps}>
          <Button>
            <Icon type='upload' /> Click to Upload An Exam
          </Button>
        </Upload>
      </div>
    )
  }

  componentDidUpdate () {
    if (this.state.up) {
      this.voteUp()
    } else if (this.state.down) {
      this.voteDown()
    }
  }
}

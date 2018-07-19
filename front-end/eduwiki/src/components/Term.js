import React from 'react'
import fetch from 'isomorphic-fetch'
import {Button, Card, Col, Row, Tooltip, Menu, Layout} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
      exams: [],
      workAssignments: [],
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termId: undefined,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined,
      voteUp: false,
      voteDown: false,
      courseId: props.courseId
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorkAssignments = this.fetchWorkAssignments.bind(this)
  }

  getExams (termId) {
    this.setState({
      examFlag: true,
      termId: termId
    })
  }

  getWorkAssignments (termId) {
    this.setState({
      workAssignmentFlag: true,
      termId: termId
    })
  }

  render () {
    return (
      <div id={`term_${this.props.term.id}`}>
        <button id={`exam_button_term${this.props.term.id}`} onClick={() => this.getExams(this.props.term.id)}>Exams</button>
        <div id={`exms_term_${this.props.term.id}`}>
          {this.state.exams.map(exam => {
            if (exam.termId === this.props.term.id) {
              return (
                <div style={{ padding: '30px' }}>
                  <Row gutter={16}>
                    <Col span={8} key={exam.id}>
                      <Card title={`${exam.type} - ${exam.phase} - ${exam.dueDate}`}>
                        {exam.sheet}
                      </Card>
                    </Col>
                  </Row>
                </div>
              )
            }
            return undefined
          }
          )}
        </div>
        <button id={`wrs_button_term${this.props.term.id}`} onClick={() => this.getWorkAssignments(this.props.term.id)}>WorkAssignments</button>
        <div id={`wrs_term_${this.props.term.id}`}>
          {this.state.workAssignments.map(wrs => {
            if (wrs.termId === this.props.term.id) {
              return (
                <div style={{ padding: '30px' }}>
                  <Row gutter={16}>
                    <Col span={8} key={wrs.id}>
                      <Card title={`${wrs.type} - ${wrs.individual} - ${wrs.lateDelivery}`}>
                        {wrs.sheet}
                      </Card>
                    </Col>
                  </Row>
                </div>
              )
            }
            return undefined
          })}
        </div>
      </div>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const id = this.props.courseId
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(uri, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => ({
          voteUp: false,
          votes: prevState.votes + 1
        }))
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down',
      created_by: 'ze'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(uri, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => ({
          voteDown: false,
          votes: prevState.votes - 1
        }))
      })
  }

  componentDidUpdate () {
    if (this.state.examFlag) {
      const courseId = this.props.courseId
      this.fetchExams(courseId, this.state.termId)
    } else if (this.state.workAssignmentFlag) {
      const courseId = this.props.courseId
      this.fetchWorkAssignments(courseId, this.state.termId)
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }

  fetchExams (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/exams`
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(exams => this.setState(
        {
          examFlag: false,
          exams: exams
        }
      ))
      .catch(error => this.setState(
        {
          examFlag: false,
          examError: error
        }
      ))
  }

  fetchWorkAssignments (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/workAssignments`
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(works => this.setState(
        {
          workAssignmentFlag: false,
          workAssignments: works
        }
      ))
      .catch(error => this.setState(
        {
          workAssignmentFlag: false,
          workAssignmentError: error
        }
      ))
  }
}

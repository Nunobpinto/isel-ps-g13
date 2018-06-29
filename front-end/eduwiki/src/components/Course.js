import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import Layout from './Layout'
import {Button, Card, Col, Row, Tooltip} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
      terms: [],
      exams: [],
      classes: [],
      workAssignments: [],
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termId: undefined,
      courseError: undefined,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined,
      voteUp: false,
      voteDown: false
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorkAssignments = this.fetchWorkAssignments.bind(this)
  }

  getExams (termId) {
    var div = document.getElementById(`exms_term_${termId}`)
    if (div.style.display === 'none') {
      div.style.display = 'block'
    } else {
      div.style.display = 'none'
      this.setState({
        examFlag: true,
        termId: termId
      })
    }
  }

  getWorkAssignments (termId) {
    this.setState({
      workAssignmentFlag: true,
      termId: termId
    })
  }

  render () {
    return (
      <div>
        <Layout>
          {this.state.courseError
            ? <p> Error getting this course, please try again !!! </p>
            : <div>
              <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
              <div>
                <p>Created By : {this.state.createdBy}</p>
                <p>
                  Votes : {this.state.votes}
                  <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
                  </Tooltip>
                  <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
                  </Tooltip>
                </p>
                {this.state.termError
                  ? <p>this.state.termError </p>
                  : <ul>
                    {this.state.terms.map(item =>
                      <div>
                        <li key={item.id}>
                          {item.shortName}
                        </li>
                        <button id={`exam_button_term${item.id}`} key={item.id} onClick={() => this.getExams(item.id)}>Exams</button>
                        <div id={`exms_term_${item.id}`}>
                          {this.state.exams.map(exam => {
                            if (exam.termId === item.id) {
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
                        <button id={`wrs_button_term${item.id}`} key={item.id} onClick={() => this.getWorkAssignments(item.id)}>WorkAssignments</button>
                        <div id={`wrs_term_${item.id}`}>
                          {this.state.workAssignments.map(wrs => {
                            if (wrs.termId === item.id) {
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
                    )}
                  </ul>
                }
              </div>
            </div>
          }
        </Layout>
      </div>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
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

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id
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
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        const termsUri = `http://localhost:8080/courses/${json.id}/terms`
        fetch(termsUri, header)
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
          .then(terms =>
            this.setState({
              full_name: json.fullName,
              short_name: json.shortName,
              createdBy: json.createdBy,
              timestamp: json.timestamp,
              votes: json.votes,
              terms: terms
            }))
          .catch(err => {
            this.setState({termError: err})
          })
      })
      .catch(error => {
        this.setState({
          courseError: error
        })
      })
  }

  componentDidUpdate () {
    if (this.state.examFlag) {
      const courseId = this.props.match.params.id
      this.fetchExams(courseId, this.state.termId)
    } else if (this.state.workAssignmentFlag) {
      const courseId = this.props.match.params.id
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

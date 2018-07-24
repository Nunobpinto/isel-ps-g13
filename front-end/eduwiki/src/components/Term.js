import React from 'react'
import fetch from 'isomorphic-fetch'
import {Layout, Menu, Card, Col, Row} from 'antd'
import Exams from './Exams'

const {Content} = Layout

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
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
      <Layout style={{ background: '#fff' }}>
        <Menu
          theme='light'
          mode='horizontal'
        >
          <Menu.Item
            key={1}
            onClick={() => this.getExams(this.props.term.termId)}
          >
            Exams
          </Menu.Item>
          <Menu.Item
            key={2}
            onClick={() => this.getWorkAssignments(this.props.term.termId)}
          >
            Work Assignments
          </Menu.Item>
        </Menu>
        <Content style={{ padding: '0 24px', minHeight: 280 }}>
          {this.state.exams &&
            <Exams exams={this.state.exams} termId={this.state.termId} courseId={this.props.courseId} />
          }
        </Content>
      </Layout>
    )
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

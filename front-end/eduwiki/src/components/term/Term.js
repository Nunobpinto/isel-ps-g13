import React from 'react'
import fetcher from '../../fetcher'
import {Layout, Menu, Card, Col, Row, message} from 'antd'
import Exams from '../exams/Exams'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

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
          defaultSelectedKeys={['1']}
          defaultOpenKeys={['1']}
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
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(exams => this.setState(
        {
          examFlag: false,
          exams: exams
        }
      ))
      .catch(error => {
        message.error('Error fetching exams')
        this.setState(
          {
            examFlag: false,
            examError: error
          }
        )
      })
  }

  fetchWorkAssignments (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/workAssignments`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(works => this.setState(
        {
          workAssignmentFlag: false,
          workAssignments: works
        }
      ))
      .catch(error => {
        message.error('Error fetching work assignments')
        this.setState(
          {
            workAssignmentFlag: false,
            workAssignmentError: error
          }
        )
      })
  }
}

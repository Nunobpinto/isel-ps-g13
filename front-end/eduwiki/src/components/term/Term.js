import React from 'react'
import fetcher from '../../fetcher'
import {Layout, Menu, Card, Col, Row, message} from 'antd'
import Exams from '../exams/Exams'
import WorkAssignments from '../workAssignments/WorkAssignments'
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
      courseId: props.courseId,
      showExam: false,
      showWrs: false,
      exams: [],
      works: []
    }
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorkAssignments = this.fetchWorkAssignments.bind(this)
  }

  getExams () {
    this.setState({
      showExam: true,
      showWrs: false
    })
  }

  getWorkAssignments () {
    this.setState({
      showWrs: true,
      showExam: false
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
            onClick={() => this.getExams()}
          >
            Exams
          </Menu.Item>
          <Menu.Item
            key={2}
            onClick={() => this.getWorkAssignments()}
          >
            Work Assignments
          </Menu.Item>
        </Menu>
        <Content style={{ padding: '0 24px', minHeight: 280 }}>
          {this.state.showExam &&
            <Exams exams={this.state.exams} termId={this.props.term.termId} courseId={this.props.courseId} />
          }
          {this.state.showWrs &&
            <WorkAssignments works={this.state.works} termId={this.props.term.termId} courseId={this.props.courseId} />
          }
        </Content>
      </Layout>
    )
  }

  componentDidUpdate () {
    if (this.state.voteUp) {
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
          exams: exams.examList,
          showExam: true
        }
      ))
      .catch(error => {
        message.error('Error fetching exams')
        this.setState(
          {
            examError: error
          }
        )
      })
  }

  fetchWorkAssignments (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/work-assignments`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(works => this.setState({works: works.workAssignmentList}
      ))
      .catch(error => {
        message.error('Error fetching work assignments')
        this.setState({workAssignmentError: error}
        )
      })
  }
  componentDidMount () {
    const courseId = this.props.courseId
    const termId = this.props.term.termId
    this.fetchExams(courseId, termId)
    this.fetchWorkAssignments(courseId, termId)
  }
}

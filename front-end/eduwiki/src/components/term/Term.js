import React from 'react'
import fetcher from '../../fetcher'
import {Layout, Menu, Card, Col, Row, message} from 'antd'
import Exams from '../exams/Exams'
import WorkAssignments from '../workAssignments/WorkAssignments'
import CourseClassesOfTerm from '../classes/CourseClassesOfTerm'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const {Content} = Layout

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courseId: props.courseId,
      termId: props.term.termId,
      courseBeingFollowed: props.courseBeingFollowed,
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined,
      voteUp: false,
      voteDown: false,
      showExam: false,
      showWrs: false,
      showClasses: false,
      exams: [],
      works: [],
      classes: []
    }
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.getCourseClasses = this.getCourseClasses.bind(this)
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorkAssignments = this.fetchWorkAssignments.bind(this)
    this.fetchCourseClasses = this.fetchCourseClasses.bind(this)
  }

  getExams () {
    this.setState({
      showExam: true,
      showWrs: false,
      showClasses: false
    })
  }

  getWorkAssignments () {
    this.setState({
      showWrs: true,
      showExam: false,
      showClasses: false
    })
  }

  getCourseClasses () {
    this.setState({
      showClasses: true,
      showExam: false,
      showWrs: false
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
          <Menu.Item
            key={3}
            onClick={() => this.getCourseClasses()}
          >
            Classes
          </Menu.Item>
        </Menu>
        <Content style={{ padding: '0 24px', minHeight: 280 }}>
          {this.state.showExam &&
            <Exams exams={this.state.exams} termId={this.state.termId} courseId={this.state.courseId} />
          }
          {this.state.showWrs &&
            <WorkAssignments works={this.state.works} termId={this.state.termId} courseId={this.state.courseId} />
          }
          {this.state.showClasses &&
            <CourseClassesOfTerm
              classes={this.state.classes}
              termId={this.state.termId}
              courseId={this.state.courseId}
              courseBeingFollowed={this.state.courseBeingFollowed}
            />
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

  fetchCourseClasses (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/classes`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(classes => this.setState({classes: classes.classList}
      ))
      .catch(error => {
        message.error('Error fetching Course Classes')
        this.setState({workAssignmentError: error}
        )
      })
  }
  componentDidMount () {
    const courseId = this.props.courseId
    const termId = this.props.term.termId
    this.fetchExams(courseId, termId)
    this.fetchWorkAssignments(courseId, termId)
    this.fetchCourseClasses(courseId, termId)
  }
}

import React from 'react'
import {Layout, Menu} from 'antd'
import Exams from '../exams/Exams'
import WorkAssignments from '../workAssignments/WorkAssignments'
import CourseClassesOfTerm from '../classes/CourseClassesOfTerm'
const {Content} = Layout

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      examFlag: false,
      showExam: false,
      showWrs: false,
      showClasses: false
    }
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.getCourseClasses = this.getCourseClasses.bind(this)
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
            <Exams
              userRole={this.props.userRole}
              exams={this.props.exams}
              termId={this.props.termId}
              courseId={this.props.courseId}
            />
          }
          {this.state.showWrs &&
            <WorkAssignments
              userRole={this.props.userRole}
              works={this.props.works}
              termId={this.props.termId}
              courseId={this.props.courseId} />
          }
          {this.state.showClasses &&
            <CourseClassesOfTerm
              classes={this.props.classes}
              termId={this.props.termId}
              courseId={this.props.courseId}
              courseBeingFollowed={this.props.courseBeingFollowed}
            />
          }
        </Content>
      </Layout>
    )
  }
  componentDidMount () {
    this.getExams()
  }
}

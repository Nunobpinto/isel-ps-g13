import React from 'react'
import Term from './Term'
import {Menu, Layout, message} from 'antd'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
const cookies = new Cookies()
const { Content, Sider } = Layout

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      termId: undefined,
      exams: [],
      classes: [],
      works: [],
      terms: []
    }
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorks = this.fetchWorks.bind(this)
    this.fetchClasses = this.fetchClasses.bind(this)
  }

  fetchExams () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.state.termId}/exams`
    fetcher(url, options)
      .then(json => this.setState({exams: json.examList}))
      .catch(_ => message.error('Error fetching exams'))
  }

  fetchWorks () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.state.termId}/work-assignments`
    fetcher(url, options)
      .then(json => this.setState({works: json.workAssignmentList}))
      .catch(_ => message.error('Error fetching work assignments'))
  }

  fetchClasses () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.state.termId}/classes`
    fetcher(url, options)
      .then(json => this.setState({classes: json.courseClassList}))
      .catch(_ => message.error('Error fetching classes'))
  }

  render () {
    return (
      <Layout style={{ padding: '24px 0', background: '#fff' }}>
        <Sider width={200} style={{ background: '#fff' }}>
          <Menu
            mode='inline'
            style={{ height: '100%' }}
          >
            {this.state.terms.map(item =>
              <Menu.Item
                key={item.termId}
                onClick={() => this.setState({
                  refreshTerm: true,
                  termId: item.termId
                })}
              >
                {item.shortName}
              </Menu.Item>

            )}
          </Menu>
        </Sider>
        <Content style={{ padding: '0 24px', minHeight: 280 }}>
          {this.state.termId
            ? <Term
              exams={this.state.exams}
              works={this.state.works}
              classes={this.state.classes}
              termId={this.state.termId}
              courseId={this.props.courseId}
              courseBeingFollowed={this.state.userFollowing}
              userRole={this.props.userRole}
            />
            : <h1>Please choose one of the available Terms</h1>
          }
        </Content>
      </Layout>
    )
  }
  componentDidUpdate () {
    if (this.state.refreshTerm) {
      this.fetchExams()
      this.fetchWorks()
      this.fetchClasses()
      this.setState({refreshTerm: false})
    }
  }
  componentDidMount () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms`
    fetcher(url, options)
      .then(json => this.setState({terms: json.termList}))
      .catch(_ => message.error('Error fetching terms'))
  }
}

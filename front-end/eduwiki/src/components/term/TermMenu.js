import React from 'react'
import Term from './Term'
import {Menu, Layout, message} from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'
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
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.state.termId}/exams`
    return fetcher(url, options)
      .then(json => json.examList)
      .catch(_ => [])
  }

  fetchWorks () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.state.termId}/work-assignments`
    return fetcher(url, options)
      .then(json => json.workAssignmentList)
      .catch(_ => [])
  }

  fetchClasses () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.state.termId}/classes`
    return fetcher(url, options)
      .then(json => json.classList)
      .catch(_ => [])
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
          {this.state.canRenderTerm
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
        .then(exams =>
          this.fetchWorks()
            .then(works =>
              this.fetchClasses()
                .then(classes => this.setState({
                  exams: exams,
                  works: works,
                  classes: classes,
                  canRenderTerm: true,
                  refreshTerm: false
                }))
            )
        )
    }
  }
  componentDidMount () {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms`
    fetcher(url, options)
      .then(json => this.setState({terms: json.termList}))
      .catch(error => message.error(error.detail))
  }
}

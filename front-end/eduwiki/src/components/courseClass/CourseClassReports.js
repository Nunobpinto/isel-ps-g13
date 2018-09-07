import React from 'react'
import fetcher from '../../fetcher'
import { List, message } from 'antd'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import timestampParser from '../../timestampParser'
import config from '../../config'

class CourseClassReports extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: [{}],
      loading: true,
      className: '',
      courseName: '',
      termShortName: ''
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.approve = this.approve.bind(this)
    this.reject = this.reject.bind(this)
  }
  render () {
    return (
      <List
        itemLayout='vertical'
        bordered
        loading={this.state.loading}
        header={<div><h1>
          {this.state.termShortName}/{this.state.className}/{this.state.courseName} reports
        </h1></div>}
        dataSource={this.state.reports}
        renderItem={item => (
          <List.Item
            actions={[
              <IconText
                type='like-o'
                id='like_btn'
                onClick={() =>
                  this.setState({
                    voteUp: true,
                    reportId: item.reportId
                  })}
              />,
              <IconText
                type='dislike-o'
                id='dislike_btn'
                onClick={() =>
                  this.setState({
                    voteDown: true,
                    reportId: item.reportId
                  })}
              />
            ]}
          >
            <List.Item.Meta
              description={`Votes: ${item.votes}`}
            />
            <h3>Reported by <a href={`/users/${item.reportedBy}`}>{item.reportedBy}</a></h3>
            {item.deletePermanently && `Delete: ${item.deletePermanently}`}
            {/* <br />
            {item.courseId && `Course: ${item.courseShortName}` } */}
            <br />
            <p>Created at {timestampParser(item.timestamp)}</p>
            {
              this.props.user.reputation.role === 'ROLE_ADMIN' &&
                <div>
                  <IconText
                    type='check-circle'
                    id='like_btn'
                    text='Approve Report'
                    onClick={() =>
                      this.setState({
                        approved: true,
                        reportId: item.reportId
                      })}
                  />
                  <IconText
                    type='close-circle'
                    id='dislike_btn'
                    text='Reject Report'
                    onClick={() =>
                      this.setState({
                        rejected: true,
                        reportId: item.reportId
                      })}
                  />
                </div>
            }
          </List.Item>
        )}
      />
    )
  }
  componentDidMount () {
    const classId = this.props.classId
    const courseId = this.props.courseId
    const url = config.API_PATH + '/classes/' + classId + '/courses/' + courseId + '/reports'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/classes/' + classId + '/courses/' + courseId, options)
      .then(klass => {
        fetcher(url, options)
          .then(list => {
            let reports = list.courseClassReportList
            reports = reports.filter(report => report.reportedBy !== this.props.user.username)
            this.setState({
              reports: reports,
              loading: false,
              className: klass.className,
              courseName: klass.courseShortName,
              termShortName: klass.lecturedTerm
            })
          })
          .catch(_ => {
            message.error('Error obtaining reports of course class')
            this.setState({loading: false})
          })
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const reportId = this.state.reportId
    const classId = this.props.classId
    const courseId = this.props.courseId
    const url = `${config.API_PATH}/classes/${classId}/courses/${courseId}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.reports]
        const index = newArray.findIndex(report => report.reportId === reportId)
        newArray[index].votes = prevState.reports[index].votes + 1
        message.success('Vote Up!!!!')
        return ({
          reports: newArray,
          voteUp: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteUp: false })
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const reportId = this.state.reportId
    const classId = this.props.classId
    const courseId = this.props.courseId
    const url = `${config.API_PATH}/classes/${classId}/courses/${courseId}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.reports]
        const index = newArray.findIndex(report => report.reportId === reportId)
        newArray[index].votes = prevState.reports[index].votes - 1
        message.success('Vote Down!!!!')
        return ({
          reports: newArray,
          voteDown: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteDown: false })
      })
  }
  approve () {
    const reportId = this.state.reportId
    const courseId = this.props.courseId
    const classId = this.props.classId
    const url = `${config.API_PATH}/classes/${classId}/courses/${courseId}/reports/${reportId}`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, body)
      .then(_ => {
        this.setState(prevState => {
          message.success('Approved report!!')
          let newArray = [...prevState.reports]
          newArray = newArray.filter(report => report.reportId !== this.state.reportId)
          return ({
            reports: newArray,
            approved: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot approve report')
        }
        this.setState({approved: false})
      })
  }

  reject () {
    const reportId = this.state.reportId
    const courseId = this.props.courseId
    const classId = this.props.classId
    const url = `${config.API_PATH}/classes/${classId}/courses/${courseId}/reports/${reportId}`
    const body = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, body)
      .then(_ =>
        this.setState(prevState => {
          message.success('Rejected report!!')
          let newArray = [...prevState.reports]
          newArray = newArray.filter(report => report.reportId !== this.state.reportId)
          return ({
            reports: newArray,
            rejected: false
          })
        })
      )
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot reject report')
        }
        this.setState({rejected: false})
      })
  }

  componentDidUpdate () {
    if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.approved) {
      this.approve()
    } else if (this.state.rejected) {
      this.reject()
    }
  }
}

export default (props) => (
  <Layout>
    <CourseClassReports classId={props.match.params.classId} courseId={props.match.params.courseId} />
  </Layout>
)

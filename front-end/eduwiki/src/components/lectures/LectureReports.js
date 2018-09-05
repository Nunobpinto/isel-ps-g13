import React from 'react'
import fetcher from '../../fetcher'
import { List, message, Button } from 'antd'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'

class LectureReports extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: [],
      loading: true,
      lecturedTerm: '',
      className: '',
      courseShortName: '',
      weekday: '',
      begins: ''
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.approve = this.approve.bind(this)
    this.reject = this.reject.bind(this)
    this.parseDuration = this.parseDuration.bind(this)
    this.parseTime = this.parseTime.bind(this)
  }
  parseDuration (duration) {
    if (duration) {
      return duration.slice(2)
    }
  }
  parseTime (time) {
    if (time) {
      const splitted = time.split(':')
      return `${splitted[0]}:${splitted[1]}`
    }
  }
  render () {
    return (
      <List
        itemLayout='vertical'
        bordered
        loading={this.state.loading}
        header={<div><h1>Lecture {this.state.lecturedTerm}/{this.state.className}/{this.state.courseShortName}/{this.state.weekday} - {this.parseTime(this.state.begins)}</h1></div>}
        footer={<div><a href={`/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/${this.props.lectureId}`}><Button>See Lecture Page</Button></a></div>}
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
              title={`Reported by ${item.reportedBy}`}
              description={`Votes: ${item.votes}`}
            />
            {item.weekDay && `Week Day: ${item.weekDay}`}
            <br />
            {item.begins && `Begins: ${this.parseTime(item.begins)}`}
            <br />
            {item.duration && `Duration: ${this.parseDuration(item.duration)}`}
            <br />
            {item.location && `Location: ${item.location}`}
            <br />
            Created at {item.timestamp}
            <br />
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
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}/reports`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(`http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}`, options)
      .then(lecture =>
        fetcher(url, options)
          .then(list => {
            let reports = list.lectureReportList
            reports = reports.filter(report => report.reportedBy !== this.props.user.username)
            this.setState({
              reports: reports,
              loading: false,
              lecturedTerm: lecture.lecturedTerm,
              className: lecture.className,
              courseShortName: lecture.courseShortName,
              weekDay: lecture.weekDay,
              begins: lecture.begins
            })
          })
          .catch(_ => {
            message.error('Error obtaining reports of lecture')
            this.setState({loading: false})
          })
      )
      .catch(_ => {
        message.error('Error finding the lecture')
        this.setState({loading: false})
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const reportId = this.state.reportId
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
      .catch(_ => {
        message.error('Error while processing your vote')
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const reportId = this.state.reportId
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
      .catch(_ => {
        message.error('Error while processing your vote')
        this.setState({voteDown: false})
      })
  }
  approve () {
    const reportId = this.state.reportId
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}/reports/${reportId}`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
    const url = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.classId}/lectures/${this.props.lectureId}/reports/${reportId}`
    const body = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
    <LectureReports
      courseId={props.match.params.courseId}
      classId={props.match.params.classId}
      lectureId={props.match.params.lectureId}
    />
  </Layout>
)

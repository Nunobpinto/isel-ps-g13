import React from 'react'
import fetcher from '../../fetcher'
import { List, message } from 'antd'
import IconText from '../comms/IconText'
import config from '../../config'

export default class UserReports extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: [],
      loading: true
    }
    this.approve = this.approve.bind(this)
    this.reject = this.reject.bind(this)
  }
  render () {
    return (
      <List
        itemLayout='vertical'
        bordered
        loading={this.state.loading}
        header={<div><h1>User Reports</h1></div>}
        dataSource={this.state.reports}
        renderItem={item => (
          <List.Item>
            <List.Item.Meta
              description={`Created at: ${item.timestamp}`}
            />
            <p>Reported by <a href={`/users/${item.reportedBy}`}>{item.reportedBy}</a></p>
            Reason: {item.reason}
            <br />
            {
              this.props.authUser.reputation.role === 'ROLE_ADMIN' &&
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
    const url = `${config.API_PATH}/users/${this.props.username}/reports`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, options)
      .then(json => {
        let reports = json.reports
        reports = reports.filter(rep => rep.reportedBy !== this.props.authUser.username)
        this.setState({
          reports: reports,
          loading: false
        })
      })
      .catch(error => {
        message.error(error.detail)
        this.setState({loading: false})
      })
  }
  approve () {
    const reportId = this.state.reportId
    const url = `${config.API_PATH}/users/${this.props.username}/reports/${reportId}`
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
    const url = `${config.API_PATH}/users/${this.props.username}/reports/${reportId}`
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
    if (this.state.approved) {
      this.approve()
    } else if (this.state.rejected) {
      this.reject()
    }
  }
}

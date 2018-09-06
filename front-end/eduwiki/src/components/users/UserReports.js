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
              description={`Created at: ${item.timestamp}`}
            />
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
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
    const url = `${config.API_PATH}/users/${this.props.username}/reports/${reportId}`
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
    if (this.state.approved) {
      this.approve()
    } else if (this.state.rejected) {
      this.reject()
    }
  }
}

import React from 'react'
import { List, message } from 'antd'
import IconText from '../comms/IconText'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import timestampParser from '../../timestampParser'
import config from '../../config'

class OrganizationReports extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: []
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
        header={<div><h1>ISEL Reports</h1></div>}
        bordered
        dataSource={this.state.reports}
        renderItem={item => (
          <div>
            {item.reportedBy !== this.props.user.username &&
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
              <p>Created at {timestampParser(item.timestampParser)}</p>
              {item.fullName && `Full name: ${item.fullName}`}
              <br />
              {item.shortName && `Short name: ${item.shortName}`}
              <br />
              {item.address && `Address: ${item.address}`}
              <br />
              {item.contact && `Contact: ${item.contact}`}
              <br />
              {item.website && `Website: ${item.website}`}
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
            }
          </div>
        )
        }
      />
    )
  }
  componentDidMount () {
    const url = config.API_PATH + '/organization/reports'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(list => this.setState({ reports: list.organizationReportList }))
      .catch(error => message.error(error.detail))
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const reportId = this.state.reportId
    const url = `${config.API_PATH}/organization/reports/${reportId}/vote`
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
      .then(_ =>
        this.setState(prevState => {
          let newArray = [...prevState.reports]
          const index = newArray.findIndex(report => report.reportId === reportId)
          newArray[index].votes = prevState.reports[index].votes + 1
          return ({
            reports: newArray,
            voteUp: false
          })
        })
      )
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const reportId = this.state.reportId
    const url = `${config.API_PATH}/organization/reports/${reportId}/vote`
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
      .then(_ =>
        this.setState(prevState => {
          let newArray = [...prevState.reports]
          const index = newArray.findIndex(report => report.reportId === reportId)
          newArray[index].votes = prevState.reports[index].votes - 1
          return ({
            reports: newArray,
            voteDown: false
          })
        })
      )
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote down')
        }
        this.setState({voteDown: false})
      })
  }

  approve () {
    const reportId = this.state.reportId
    const url = `${config.API_PATH}/organization/reports/${reportId}`
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
    const url = `${config.API_PATH}/organization/reports/${reportId}`
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
    <OrganizationReports />
  </Layout>
)

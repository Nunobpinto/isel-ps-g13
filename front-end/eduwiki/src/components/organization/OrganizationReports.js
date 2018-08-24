import React from 'react'
import { List, message } from 'antd'
import IconText from '../comms/IconText'
import Cookies from 'universal-cookie'
import fetcher from '../../fetcher'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: []
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }
  render () {
    return (
      <List
        itemLayout='vertical'
        bordered
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
            {item.fullName && `Full name: ${item.fullName}`}
            <br />
            {item.shortName && `Short name: ${item.shortName}`}
            <br />
            {item.address && `Address: ${item.address}`}
            <br />
            {item.contact && `Total Credits: ${item.contact}`}
            <br />
            {item.website && `Website: ${item.website}`}
          </List.Item>
        )}
      />
    )
  }
  componentDidMount () {
    const url = 'http://localhost:8080/organization/reports'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(list => this.setState({ reports: list.organizationReportList }))
      .catch(_ => message.error('Error obtaining reports'))
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const reportId = this.state.reportId
    const url = `http://localhost:8080/organization/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
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
      .catch(_ => {
        message.error('Cannot vote up')
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const reportId = this.state.reportId
    const url = `http://localhost:8080/organization/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
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
      .catch(_ => {
        message.error('Cannot vote down')
        this.setState({voteDown: false})
      })
  }

  componentDidUpdate () {
    if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUp) {
      this.voteUp()
    }
  }
}

import React from 'react'
import fetcher from '../../fetcher'
import { List, message } from 'antd'
import IconText from '../comms/IconText'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reports: [],
      progID: props.id
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
            {item.programmeFullName && `Full name: ${item.programmeFullName}`}
            <br />
            {item.programmeShortName && `Short name: ${item.programmeShortName}`}
            <br />
            {item.programmeAcademicDegree && `Academic Degree: ${item.programmeAcademicDegree}`}
            <br />
            {item.programmeTotalCredits && `Total Credits: ${item.programmeTotalCredits}`}
            <br />
            {item.programmeDuration && `Duration: ${item.programmeDuration}`}
          </List.Item>
        )}
      />
    )
  }
  componentDidMount () {
    const url = 'http://localhost:8080/programmes/' + this.state.progID + '/reports'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, options)
      .then(list => this.setState({ reports: list.programmeReportList }))
      .catch(_ => message.error('Error obtaining reports of programme'))
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const reportId = this.state.reportId
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/reports/${reportId}/vote`
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
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/reports/${reportId}/vote`
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

  componentDidUpdate () {
    if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUp) {
      this.voteUp()
    }
  }
}

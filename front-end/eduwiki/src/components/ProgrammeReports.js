import React from 'react'
import fetch from 'isomorphic-fetch'
import { List } from 'antd'
import IconText from './IconText'

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
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(url, options)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(list => {
        this.setState({ reports: list })
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const reportId = this.state.reportId
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.reports]
          const index = newArray.findIndex(report => report.reportId === reportId)
          newArray[index].votes = prevState.reports[index].votes + 1
          return ({
            reports: newArray,
            voteUp: false
          })
        })
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down',
      created_by: 'ze'
    }
    const reportId = this.state.reportId
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/reports/${reportId}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.reports]
          const index = newArray.findIndex(report => report.reportId === reportId)
          newArray[index].votes = prevState.reports[index].votes - 1
          return ({
            reports: newArray,
            voteDown: false
          })
        })
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

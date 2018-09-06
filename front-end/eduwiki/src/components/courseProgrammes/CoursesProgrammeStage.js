import React from 'react'
import fetcher from '../../fetcher'
import {message, List, Card} from 'antd'
import IconText from '../comms/IconText'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      staged: [],
      loading: true
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.rejectStaged = this.rejectStaged.bind(this)
  }
  render () {
    let dataSource = this.state.staged
    if (this.props.staged) {
      dataSource.push(this.props.staged)
    }
    return (
      <div>
        <h3>All staged Courses In Programme</h3>
        <List
          grid={{ gutter: 16, column: 4 }}
          loading={this.state.loading}
          dataSource={dataSource}
          renderItem={staged => (
            <List.Item>
              <Card title={staged.courseShortName}>
                <p>Lectured in {staged.lecturedTerm}</p>
                <p>Optional : {staged.optional ? 'Yes' : 'No'}</p>
                <p>Credits: {staged.credits}</p>
                <p>Votes: {staged.votes}</p>
                <p>Created By <a href={`/users/${staged.createdBy}`}>{staged.createdBy}</a></p>
                <p>Created at {timestampParser(staged.timestamp)}</p>
                <IconText
                  type='like-o'
                  id='like_btn'
                  onClick={() =>
                    this.setState({
                      voteUp: true,
                      stagedId: staged.stagedId
                    })}
                />
                <IconText
                  type='dislike-o'
                  id='dislike_btn'
                  onClick={() =>
                    this.setState({
                      voteDown: true,
                      stagedId: staged.stagedId
                    })}
                />
                {
                  this.props.isAdmin &&
                  <div>
                    <IconText
                      type='check-circle'
                      id='like_btn'
                      text='Approve Staged'
                      onClick={() =>
                        this.setState({
                          approved: true,
                          stagedId: staged.stagedId
                        })}
                    />
                    <IconText
                      type='close-circle'
                      id='dislike_btn'
                      text='Reject Staged'
                      onClick={() =>
                        this.setState({
                          rejected: true,
                          stagedId: staged.stagedId
                        })}
                    />
                  </div>
                }
              </Card>
            </List.Item>
          )}
        />,
      </div>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.programmeId
    const uri = config.API_PATH + '/programmes/' + id + '/courses/stage/' + this.state.stagedId + '/vote'
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
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Up!!')
        this.setState(prevState => {
          let staged = prevState.staged
          const idx = staged.findIndex(st => st.stagedId === prevState.stagedId)
          staged[idx].votes += 1
          return ({
            voteStagedCoursesUp: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteStagedCoursesUp: false })
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const id = this.props.programmeId
    const uri = config.API_PATH + '/programmes/' + id + '/courses/stage/' + this.state.stagedId + '/vote'
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
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Down!!')
        this.setState(prevState => {
          let staged = prevState.staged
          const idx = staged.findIndex(st => st.stagedId === prevState.stagedId)
          staged[idx].votes -= 1
          return ({
            voteStagedCoursesDown: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteStagedCoursesDown: false })
      })
  }
  approveStaged () {
    this.props.adminAction(this.state.stagedId)
    this.setState(prevState => {
      let staged = prevState.staged
      staged = staged.filter(st => st.stagedId !== this.state.stagedId)
      return ({
        approved: false,
        staged: staged
      })
    })
  }
  rejectStaged () {
    const id = this.props.programmeId
    const uri = config.API_PATH + '/programmes/' + id + '/courses/stage/' + this.state.stagedId
    const body = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Rejected staged Course Programme!!')
        this.setState(prevState => {
          let staged = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          return ({
            rejected: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while rejecting this course programme')
        }
        this.setState({ rejected: false })
      })
  }
  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.approved) {
      this.approveStaged()
    } else if (this.state.rejected) {
      this.rejectStaged()
    }
  }
  componentDidMount () {
    const id = this.props.programmeId
    const uri = `${config.API_PATH}/programmes/${id}/courses/stage`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(staged => this.setState({
        loading: false,
        staged: staged.courseProgrammeStageList
      }))
      .catch(_ => {
        message.error('Error loading staged courses of programme')
      })
  }
}

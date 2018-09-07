import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, message, List, Card } from 'antd'
import SubmitWorkAssignment from './SubmitWorkAssignment'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <AllWorkAssignments courseId={props.match.params.courseId} termId={props.match.params.termId} />
  </Layout>
)

class AllWorkAssignments extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      works: [],
      error: undefined,
      voteUpStaged: false,
      voteDownStaged: false,
      staged: [],
      course: '',
      term: '',
      loading: true
    }
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedWorkAssignment = this.createStagedWorkAssignment.bind(this)
    this.createDefinitiveWorkAssignment = this.createDefinitiveWorkAssignment.bind(this)
    this.createWorkAssignment = this.createWorkAssignment.bind(this)
    this.showResource = this.showResource.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.deleteStaged = this.deleteStaged.bind(this)
  }

  approveStaged () {
    const stagedUri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage/${this.state.stagedId}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(work => {
        message.success('Successfully approved staged programme')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          prevState.works.push(work)
          return ({
            staged: newArray,
            works: prevState.works,
            approved: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while approving staged')
        }
        this.setState({approved: false})
      })
  }
  deleteStaged () {
    const stagedUri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage/${this.state.stagedId}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(_ => {
        message.success('Successfully deleted staged programme')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          return ({
            staged: newArray,
            rejected: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while rejecting staged')
        }
        this.setState({rejected: false})
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage/${stageID}/vote`
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
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(exam => exam.stagedId === stageID)
        newArray[index].votes = prevState.staged[index].votes + 1
        message.success('Successfully voted up')
        return ({
          staged: newArray,
          voteUpStaged: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({voteUpStaged: false})
      })
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage/${stageID}/vote`
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
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(exam => exam.stagedId === stageID)
        newArray[index].votes = prevState.staged[index].votes - 1
        message.success('Successfully voted down')
        return ({
          staged: newArray,
          voteDownStaged: false
        })
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({voteDownStaged: false})
      })
  }

  render () {
    return (
      <div>
        <h1>All Work Assignments in {this.state.term} / <a href={`/courses/${this.props.courseId}`}>{this.state.course}</a></h1>
        <div >
          <div className='left-div'>
            <List
              itemLayout='vertical'
              size='large'
              bordered
              loading={this.state.loading}
              dataSource={this.state.works}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    title={<Link to={{ pathname: `/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/${item.workAssignmentId}` }}>{item.phase} - {item.dueDate}</Link>}
                    description={`Created by ${item.createdBy}`}
                  />
                </List.Item>
              )}
            />
          </div>
          <div className='right-div'>
            <div id='stagedCourses'>
              <h1>All staged Work Assignments</h1>
              <List id='staged-list'
                grid={{ gutter: 50, column: 1 }}
                dataSource={this.state.staged}
                loading={this.state.loading}
                footer={<Button type='primary' onClick={() => this.setState({createWorkFlag: true})}>Create Work Assignment</Button>}
                renderItem={item => (
                  <List.Item>
                    <Card title={`${item.phase} - ${item.dueDate}`}>
                      <p>Individual : {item.individual ? 'Yes' : 'No'}</p>
                      <p>Late Delivery : {item.lateDelivery ? 'Yes' : 'No'}</p>
                      <p>Multiple Deliveries : {item.multipleDeliveries ? 'Yes' : 'No'}</p>
                      <p>Requires Report : {item.requiresReport ? 'Yes' : 'No'}</p>
                      {item.sheetId && <Button onClick={() => this.showResource(item.sheetId)}>See sheet</Button>}
                      {item.supplementId && <Button onClick={() => this.showResource(item.supplementId)}>See supplement</Button>}
                      <p>Created By : <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                      <p>Created At {timestampParser(item.timestamp)}</p>
                      <IconText
                        type='like-o'
                        id='like_btn'
                        onClick={() =>
                          this.setState({
                            voteUpStaged: true,
                            stageID: item.stagedId
                          })}
                        text={item.votes}
                      />
                      <IconText
                        type='dislike-o'
                        id='dislike_btn'
                        onClick={() =>
                          this.setState({
                            voteDownStaged: true,
                            stageID: item.stagedId
                          })}
                      />
                      {this.props.user.reputation.role === 'ROLE_ADMIN' &&
                      <div>
                        <IconText
                          type='check-circle'
                          id='like_btn'
                          text='Approve Staged'
                          onClick={() =>
                            this.setState({
                              approved: true,
                              stagedId: item.stagedId
                            })}
                        />
                        <IconText
                          type='close-circle'
                          id='dislike_btn'
                          text='Reject Staged'
                          onClick={() =>
                            this.setState({
                              rejected: true,
                              stagedId: item.stagedId
                            })}
                        />
                      </div>
                      }
                    </Card>

                  </List.Item>
                )}
              />
              {this.state.createWorkFlag && <SubmitWorkAssignment action={(data) => this.setState({
                data: data,
                actionFlag: true
              })} />}
            </div>
          </div>
        </div>
      </div>
    )
  }

  createWorkAssignment (work) {
    if (this.props.user.reputation.role === 'ROLE_ADMIN') {
      this.createDefinitiveWorkAssignment(work)
    } else {
      this.createStagedWorkAssignment(work)
    }
  }

  createDefinitiveWorkAssignment (work) {
    const uri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments`
    let data = new FormData()
    data.append('sheet', work.sheet)
    data.append('supplement', work.supplement)
    data.append('phase', work.phase)
    data.append('dueDate', work.dueDate)
    data.append('individual', work.individual)
    data.append('lateDelivery', work.lateDelivery)
    data.append('multipleDeliveries', work.multipleDeliveries)
    data.append('requiresReport', work.requiresReport)
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      },
      body: data
    }
    fetcher(uri, options)
      .then(work => {
        message.success('Saved the Work Assignment you requested')
        this.setState(prevstate => {
          prevstate.works.push(work)
          return ({actionFlag: false, works: prevstate.works})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Work Assignment you requested')
        this.setState({actionFlag: false})
      })
  }

  createStagedWorkAssignment (work) {
    const uri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage`
    let data = new FormData()
    data.append('sheet', work.sheet)
    data.append('supplement', work.supplement)
    data.append('phase', work.phase)
    data.append('dueDate', work.dueDate)
    data.append('individual', work.individual)
    data.append('lateDelivery', work.lateDelivery)
    data.append('multipleDeliveries', work.multipleDeliveries)
    data.append('requiresReport', work.requiresReport)
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      },
      body: data
    }
    fetcher(uri, options)
      .then(work => {
        message.success('Saved the Work Assignment you requested')
        this.setState(prevstate => {
          prevstate.staged.push(work)
          return ({actionFlag: false, staged: prevstate.staged})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Work Assignment you requested')
        this.setState({actionFlag: false})
      })
  }

  showResource (sheet) {
    const resourceUrl = `${config.API_PATH}/resources/${sheet}`
    window.open(resourceUrl)
  }

  componentDidMount () {
    const uri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/courses/' + this.props.courseId, header)
      .then(course => {
        fetcher(`${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}`, header)
          .then(term => {
            fetcher(uri, header)
              .then(works => {
                const stagedUri = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/stage`
                fetcher(stagedUri, header)
                  .then(stagedWorks => this.setState({
                    works: works.workAssignmentList,
                    staged: stagedWorks.workAssignmentStageList,
                    course: course.shortName,
                    term: term.shortName,
                    loading: false
                  }))
                  .catch(stagedError => {
                    message.error('Error fetching staged work assignments')
                    this.setState({
                      works: works.workAssignmentList,
                      stagedError: stagedError,
                      course: course.shortName,
                      term: term.shortName,
                      loading: false
                    })
                  })
              })
              .catch(error => {
                message.error('Error fetching work assignments')
                this.setState({error: error})
              })
          })
          .catch(_ => message.error('Error getting term'))
      })
      .catch(_ => message.error('Error getting course'))
  }
  componentDidUpdate () {
    if (this.state.actionFlag) {
      this.createWorkAssignment(this.state.data)
    } else if (this.state.approved) {
      this.approveStaged()
    } else if (this.state.rejected) {
      this.deleteStaged()
    } else if (this.state.voteUpStaged) {
      this.voteUpStaged()
    } else if (this.state.voteDownStaged) {
      this.voteDownStaged()
    }
  }
}

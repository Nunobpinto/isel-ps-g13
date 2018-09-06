import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, message, List, Card } from 'antd'
import SubmitExam from './SubmitExam'
import timestampParser from '../../timestampParser'

export default (props) => (
  <Layout>
    <AllExams courseId={props.match.params.courseId} termId={props.match.params.termId} />
  </Layout>
)

class AllExams extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      exams: [],
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
    this.createStagedExam = this.createStagedExam.bind(this)
    this.createDefinitiveExam = this.createDefinitiveExam.bind(this)
    this.createExam = this.createExam.bind(this)
    this.showResource = this.showResource.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.deleteStaged = this.deleteStaged.bind(this)
  }
  approveStaged () {
    const stagedUri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage/${this.state.stagedId}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(stagedUri, options)
      .then(exam => {
        message.success('Successfully approved staged exam')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          prevState.exams.push(exam)
          return ({
            staged: newArray,
            exams: prevState.exams,
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
    const stagedUri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage/${this.state.stagedId}`
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(stagedUri, options)
      .then(_ => {
        message.success('Successfully deleted staged exam')
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
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage/${stageID}/vote`
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
    const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage/${stageID}/vote`
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
        <h1>All Exams in {this.state.term} / <a href={`/courses/${this.props.courseId}`}>{this.state.course}</a></h1>
        <div >
          <div className='left-div'>
            <List
              itemLayout='vertical'
              size='large'
              bordered
              loading={this.state.loading}
              dataSource={this.state.exams}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    title={<Link to={{ pathname: `/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${item.examId}` }}>{item.type} - {item.phase} - {item.dueDate}</Link>}
                    description={`Created by ${item.createdBy}`}
                  />
                </List.Item>
              )}
            />
          </div>
          <div className='right-div'>
            <div id='stagedCourses'>
              <h1>All staged Exams</h1>
              <List id='staged-list'
                grid={{ gutter: 50, column: 1 }}
                dataSource={this.state.staged}
                loading={this.state.loading}
                footer={<Button type='primary' onClick={() => this.setState({createExamFlag: true})}>Create Exam</Button>}
                renderItem={item => (
                  <List.Item>
                    <Card title={`${item.type} - ${item.phase}`}>
                      {item.sheetId && <Button onClick={() => this.showResource(item.sheetId)}>See resource</Button>}
                      <p>Due Date: {item.dueDate}</p>
                      <p>Created By: <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                      <p>Created At: {timestampParser(item.timestamp)}</p>
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
              {this.state.createExamFlag && <SubmitExam action={(data) => this.setState({
                data: data,
                actionFlag: true
              })} />}
            </div>
          </div>
        </div>
      </div>
    )
  }

  createExam (exam) {
    if (this.props.user.reputation.role === 'ROLE_ADMIN') {
      this.createDefinitiveExam(exam)
    } else {
      this.createStagedExam(exam)
    }
  }

  createDefinitiveExam (exam) {
    const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams`
    let data = new FormData()
    data.append('sheet', exam.file)
    data.append('phase', exam.phase)
    data.append('dueDate', exam.dueDate)
    data.append('type', exam.type)
    data.append('location', exam.location)
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: data
    }
    fetcher(uri, options)
      .then(exam => {
        message.success('Saved the Exam you requested')
        this.setState(prevstate => {
          prevstate.exams.push(exam)
          return ({actionFlag: false, exams: prevstate.exams})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Exam you requested')
        this.setState({actionFlag: false})
      })
  }

  createStagedExam (exam) {
    const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage`
    let data = new FormData()
    data.append('sheet', exam.file)
    data.append('phase', exam.phase)
    data.append('dueDate', exam.dueDate)
    data.append('type', exam.type)
    data.append('location', exam.location)
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: data
    }
    fetcher(uri, options)
      .then(exam => {
        message.success('Saved the Exam you requested')
        this.setState(prevstate => {
          prevstate.staged.push(exam)
          return ({actionFlag: false, staged: prevstate.staged})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Exam you requested')
        this.setState({actionFlag: false})
      })
  }

  showResource (sheet) {
    const resourceUrl = `http://localhost:8080/resources/${sheet}`
    window.open(resourceUrl)
  }

  componentDidMount () {
    const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/courses/' + this.props.courseId, header)
      .then(course => {
        fetcher(`http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}`, header)
          .then(term => {
            fetcher(uri, header)
              .then(exams => {
                const stagedUri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/stage`
                fetcher(stagedUri, header)
                  .then(stagedExams => this.setState({
                    exams: exams.examList,
                    staged: stagedExams.examStageList,
                    course: course.shortName,
                    term: term.shortName,
                    loading: false
                  }))
                  .catch(stagedError => {
                    message.error('Error fetching staged exams')
                    this.setState({
                      exams: exams.examList,
                      stagedError: stagedError,
                      course: course.shortName,
                      term: term.shortName,
                      loading: false
                    })
                  })
              })
              .catch(error => {
                message.error('Error fetching exams')
                this.setState({error: error})
              })
          })
          .catch(_ => message.error('Error getting term'))
      })
      .catch(_ => message.error('Error getting course'))
  }
  componentDidUpdate () {
    if (this.state.actionFlag) {
      this.createExam(this.state.data)
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

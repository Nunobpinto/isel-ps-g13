import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, message, List, Card } from 'antd'
import SubmitLecture from './SubmitLecture'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <AllLecturesPage courseId={props.match.params.courseId} classId={props.match.params.classId} />
  </Layout>
)

class AllLecturesPage extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      lectures: [],
      error: undefined,
      voteUpStaged: false,
      voteDownStaged: false,
      staged: [],
      course: '',
      term: '',
      class: '',
      loading: true
    }
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedLecture = this.createStagedLecture.bind(this)
    this.createDefinitiveLecture = this.createDefinitiveLecture.bind(this)
    this.createLecture = this.createLecture.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.deleteStaged = this.deleteStaged.bind(this)
    this.parseDuration = this.parseDuration.bind(this)
    this.parseTime = this.parseTime.bind(this)
  }

  approveStaged () {
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage/${this.state.stagedId}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(lecture => {
        message.success('Successfully approved staged lecture')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          prevState.lectures.push(lecture)
          return ({
            staged: newArray,
            lectures: prevState.lectures,
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage/${this.state.stagedId}`
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
        message.success('Successfully deleted staged lecture')
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage/${this.state.stagedId}/vote`
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
    fetcher(stagedUri, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(hw => hw.stagedId === prevState.stagedId)
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage/${this.state.stagedId}/vote`
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
    fetcher(stagedUri, body)
      .then(_ => this.setState(prevState => {
        let newArray = [...prevState.staged]
        const index = newArray.findIndex(hw => hw.stagedId === prevState.stagedId)
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
        <h1>All Lectures in {this.state.term}/{this.state.class}/{this.state.course}</h1>
        <div >
          <div className='left-div'>
            <List
              itemLayout='vertical'
              size='large'
              bordered
              footer={<a href={`/classes/${this.props.classId}/courses/${this.props.courseId}`}><Button>Go back to Course Class</Button></a>}
              loading={this.state.loading}
              dataSource={this.state.lectures}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    title={<Link to={{ pathname: `/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/${item.lectureId}` }}>{item.weekDay} - {this.parseTime(item.begins)}</Link>}
                    description={`Created by ${item.createdBy}`}
                  />
                </List.Item>
              )}
            />
          </div>
          <div className='right-div'>
            <div id='stagedCourses'>
              <h1>All staged Lectures</h1>
              <List id='staged-list'
                grid={{ gutter: 50, column: 1 }}
                dataSource={this.state.staged}
                loading={this.state.loading}
                footer={<Button type='primary' onClick={() => this.setState({createLectureFlag: true})}>Create Lecture</Button>}
                renderItem={item => (
                  <List.Item>
                    <Card title={`${item.weekDay}`}>
                      <p>Created By : <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                      <p>Begins : {this.parseTime(item.begins)}</p>
                      <p>Duration : {this.parseDuration(item.duration)}</p>
                      <p>Location : {item.location}</p>
                      <p>Created At: {timestampParser(item.timestamp)}</p>
                      <IconText
                        type='like-o'
                        id='like_btn'
                        onClick={() =>
                          this.setState({
                            voteUpStaged: true,
                            stagedId: item.stagedId
                          })}
                        text={item.votes}
                      />
                      <IconText
                        type='dislike-o'
                        id='dislike_btn'
                        onClick={() =>
                          this.setState({
                            voteDownStaged: true,
                            stagedId: item.stagedId
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
              {this.state.createLectureFlag && <SubmitLecture action={(data) => this.setState({
                data: data,
                actionFlag: true
              })} />}
            </div>
          </div>
        </div>
      </div>
    )
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
  createLecture (lecture) {
    if (this.props.user.reputation.role === 'ROLE_ADMIN') {
      this.createDefinitiveLecture(lecture)
    } else {
      this.createStagedLecture(lecture)
    }
  }

  createDefinitiveLecture (data) {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'Content-Type': 'application/json',
        'tenant-uuid': config.TENANT_UUID
      },
      body: JSON.stringify(data)
    }
    fetcher(uri, options)
      .then(lecture => {
        message.success('Saved the Lecture you requested')
        this.setState(prevstate => {
          prevstate.lectures.push(lecture)
          return ({actionFlag: false, lectures: prevstate.lectures})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Lecture you requested')
        this.setState({actionFlag: false})
      })
  }

  createStagedLecture (data) {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'Content-Type': 'application/json',
        'tenant-uuid': config.TENANT_UUID
      },
      body: JSON.stringify(data)
    }
    fetcher(uri, options)
      .then(lecture => {
        message.success('Saved the Lecture you requested')
        this.setState(prevstate => {
          prevstate.staged.push(lecture)
          return ({actionFlag: false, staged: prevstate.staged})
        }
        )
      })
      .catch(_ => {
        message.error('Error while saving the Lecture you requested')
        this.setState({actionFlag: false})
      })
  }

  componentDidMount () {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(`${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}`, header)
      .then(courseClass => {
        fetcher(uri, header)
          .then(lectures => {
            const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/stage`
            fetcher(stagedUri, header)
              .then(stagedLectures => this.setState({
                lectures: lectures.lectureList,
                staged: stagedLectures.lectureStageList,
                course: courseClass.courseShortName,
                term: courseClass.lecturedTerm,
                class: courseClass.className,
                loading: false
              }))
              .catch(stagedError => {
                message.error('Error fetching staged lectures')
                this.setState({
                  lectures: lectures.lectureList,
                  stagedError: stagedError,
                  course: courseClass.courseshortName,
                  term: courseClass.lecturedTerm,
                  className: courseClass.className,
                  loading: false
                })
              })
          })
          .catch(error => {
            message.error('Error fetching lectures')
            this.setState({error: error})
          })
      })
      .catch(_ => message.error('Error loading the course class'))
  }
  componentDidUpdate () {
    if (this.state.actionFlag) {
      this.createLecture(this.state.data)
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

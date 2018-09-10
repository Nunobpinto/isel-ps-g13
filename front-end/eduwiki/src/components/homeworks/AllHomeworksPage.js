import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, message, List, Card } from 'antd'
import SubmitHomework from './SubmitHomework'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <AllHomeworks courseId={props.match.params.courseId} classId={props.match.params.classId} />
  </Layout>
)

class AllHomeworks extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      homeworks: [],
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
    this.createStagedHomework = this.createStagedHomework.bind(this)
    this.createDefinitiveHomework = this.createDefinitiveHomework.bind(this)
    this.createHomework = this.createHomework.bind(this)
    this.showResource = this.showResource.bind(this)
    this.approveStaged = this.approveStaged.bind(this)
    this.deleteStaged = this.deleteStaged.bind(this)
  }
  approveStaged () {
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage/${this.state.stagedId}`
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(stagedUri, options)
      .then(homework => {
        message.success('Successfully approved staged homework')
        this.setState(prevState => {
          const newArray = prevState.staged.filter(st => st.stagedId !== prevState.stagedId)
          prevState.homeworks.push(homework)
          return ({
            staged: newArray,
            homeworks: prevState.homeworks,
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage/${this.state.stagedId}`
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
        message.success('Successfully deleted staged homework')
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage/${this.state.stagedId}/vote`
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
    const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage/${this.state.stagedId}/vote`
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
        <h1>All Homeworks in {this.state.term}/{this.state.class}/{this.state.course}</h1>
        <div >
          <div className='left-div'>
            <List
              itemLayout='vertical'
              size='large'
              bordered
              footer={<a href={`/classes/${this.props.classId}/courses/${this.props.courseId}`}><Button>Go back to Course Class</Button></a>}
              loading={this.state.loading}
              dataSource={this.state.homeworks}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    title={<Link to={{ pathname: `/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/${item.homeworkId}` }}>{item.homeworkName} - {item.dueDate}</Link>}
                    description={`Created by ${item.createdBy}`}
                  />
                </List.Item>
              )}
            />
          </div>
          <div className='right-div'>
            <div id='stagedCourses'>
              <h1>All staged Homeworks</h1>
              <List id='staged-list'
                grid={{ gutter: 50, column: 1 }}
                dataSource={this.state.staged}
                loading={this.state.loading}
                footer={<Button type='primary' onClick={() => this.setState({createHomeworkFlag: true})}>Create homework</Button>}
                renderItem={item => (
                  <List.Item>
                    <Card title={`${item.homeworkName}  - ${item.dueDate}`}>
                      <p>Late Delivery : {item.lateDelivery ? 'Yes' : 'No'}</p>
                      <p>Multiple Deliveries : {item.multipleDeliveries ? 'Yes' : 'No'}</p>
                      {item.sheetId && <Button onClick={() => this.showResource(item.sheetId)}>See resource</Button>}
                      <p>Created By : <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                      <p>Added at : {timestampParser(item.timestamp)}</p>
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
              {this.state.createHomeworkFlag && <SubmitHomework action={(data) => this.setState({
                data: data,
                actionFlag: true
              })} />}
            </div>
          </div>
        </div>
      </div>
    )
  }

  createHomework (homework) {
    if (this.props.user.reputation.role === 'ROLE_ADMIN') {
      this.createDefinitiveHomework(homework)
    } else {
      this.createStagedHomework(homework)
    }
  }

  createDefinitiveHomework (homework) {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks`
    let data = new FormData()
    data.append('sheet', homework.file)
    data.append('multipleDeliveries', homework.multipleDeliveries)
    data.append('dueDate', homework.dueDate)
    data.append('homeworkName', homework.homeworkName)
    data.append('lateDelivery', homework.lateDelivery)
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
      .then(homework => {
        message.success('Saved the Homework you requested')
        this.setState(prevstate => {
          prevstate.homeworks.push(homework)
          return ({actionFlag: false, homeworks: prevstate.homeworks})
        }
        )
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while saving the Homework you requested')
        }
        this.setState({actionFlag: false})
      })
  }

  createStagedHomework (homework) {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage`
    let data = new FormData()
    data.append('sheet', homework.file)
    data.append('multipleDeliveries', homework.multipleDeliveries)
    data.append('dueDate', homework.dueDate)
    data.append('homeworkName', homework.homeworkName)
    data.append('lateDelivery', homework.lateDelivery)
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
      .then(homework => {
        message.success('Saved the Homework you requested')
        this.setState(prevstate => {
          prevstate.staged.push(homework)
          return ({actionFlag: false, staged: prevstate.staged})
        }
        )
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while saving the Homework you requested')
        }
        this.setState({actionFlag: false})
      })
  }

  showResource (sheet) {
    const resourceUrl = `${config.API_PATH}/resources/${sheet}`
    window.open(resourceUrl)
  }

  componentDidMount () {
    const uri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks`
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
          .then(homeworks => {
            const stagedUri = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks/stage`
            fetcher(stagedUri, header)
              .then(stagedHomeworks => this.setState({
                homeworks: homeworks.homeworkList,
                staged: stagedHomeworks.homeworkStageList,
                course: courseClass.courseShortName,
                term: courseClass.lecturedTerm,
                class: courseClass.className,
                loading: false
              }))
              .catch(stagedError => {
                message.error(stagedError.detail)
                this.setState({
                  homeworks: homeworks.homeworkList,
                  stagedError: stagedError,
                  course: courseClass.courseshortName,
                  term: courseClass.lecturedTerm,
                  className: courseClass.className,
                  loading: false
                })
              })
          })
          .catch(error => {
            message.error(error.detail)
            this.setState({error: error})
          })
      })
      .catch(_ => message.error('Error loading the course class'))
  }
  componentDidUpdate () {
    if (this.state.actionFlag) {
      this.createHomework(this.state.data)
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

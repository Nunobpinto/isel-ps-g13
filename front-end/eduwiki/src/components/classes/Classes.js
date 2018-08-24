import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, Input, message, List, Card } from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: [{
        term: undefined,
        classes: []
      }],
      viewClasses: [{
        term: undefined,
        classes: []
      }],
      error: undefined,
      voteUp: false,
      voteDown: false,
      staged: [],
      viewStaged: [],
      nameFilter: '',
      stagedNameFilter: ''
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedCourse = this.createStagedCourse.bind(this)
    this.filterClassesByName = this.filterClassesByName.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.reorderClasses = this.reorderClasses.bind(this)
  }

  filterClassesByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = prevState.classes
      array = array.filter(course => course.fullName.toLowerCase().includes(name))
      return ({viewCourses: array})
    })
  }

  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = prevState.classes
      array = array.filter(staged => staged.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }

  render () {
    return (
      <Layout>
        <div className='container'>
          <div className='left-div'>
            {this.state.error
              ? <p> Error getting all the classes please try again !!! </p>
              : <div>
                <h1>All classes in ISEL</h1>
                <List
                  itemLayout='vertical'
                  size='large'
                  bordered
                  dataSource={this.state.viewClasses}
                  renderItem={item => (
                    <List.Item>
                      <Card title={item.term}>
                        <List
                          itemLayout='vertical'
                          bordered
                          dataSource={item.classes}
                          renderItem={klass => (
                            <List.Item
                              actions={[
                                <IconText
                                  type='like-o'
                                  id='like_btn'
                                  onClick={() =>
                                    this.setState({
                                      voteUp: true,
                                      courseID: klass.classId
                                    })}
                                  text={klass.votes}
                                />,
                                <IconText
                                  type='dislike-o'
                                  id='dislike_btn'
                                  onClick={() =>
                                    this.setState({
                                      voteDown: true,
                                      courseID: klass.classId
                                    })}
                                />
                              ]}
                            >
                              <List.Item.Meta
                                title={<Link to={{ pathname: `/classes/${klass.classId}` }}>{klass.className}</Link>}
                                description={`Created by ${klass.createdBy}`}
                              />
                            </List.Item>
                          )}
                        />
                      </Card>
                    </List.Item>
                  )}
                />
              </div>
            }
            <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({stagedCourseView: true})}>Create Course</Button>
          </div>
          <div class='right-div'>
            {
              this.state.stagedCourseView &&
              <div id='stagedCourses'>
                <h1>All staged Courses</h1>
                <p> Filter By Name : </p>
                <Input.Search
                  name='stagedNameFilter'
                  placeholder='Search name'
                  onChange={this.filterStagedByName}

                />
                <List id='staged-list'
                  grid={{ gutter: 50, column: 2 }}
                  dataSource={this.state.viewStaged}
                  renderItem={item => (
                    <List.Item>
                      <Card title={item.fullName}>
                        <p>Short Name : {item.shortName}</p>
                        <p>Created By : {item.createdBy}</p>
                      </Card>
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
                    </List.Item>
                  )}
                />
                <Button type='primary' onClick={() => this.setState({createCourseForm: true})}>Still want to create?</Button>
              </div>
            }
          </div>
        </div>
      </Layout>
    )
  }

  reorderClasses (data) {
    let newArray = []
    data.forEach(value => {
      const index = newArray.findIndex(element => element.term === value.lecturedTerm)
      if (index === -1) {
        let obj = {
          term: value.lecturedTerm,
          classes: [{
            classId: value.classId,
            createdBy: value.createdBy,
            className: value.className,
            termId: value.termId,
            votes: value.votes,
            timestamp: value.timestamp
          }]
        }
        newArray.push(obj)
      } else {
        newArray[index].classes.push({
          classId: value.classId,
          createdBy: value.createdBy,
          className: value.className,
          termId: value.termId,
          votes: value.votes,
          timestamp: value.timestamp
        })
      }
    })
    return newArray
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/classes/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(classes => {
        let orderedClasses = this.reorderClasses(classes.classList)
        this.setState({
          classes: orderedClasses,
          viewClasses: orderedClasses
        })
      })
      .catch(error => {
        message.error('Error fetching classes')
        this.setState({error: error})
      })
  }

  createStagedCourse (data) {
    const body = {
      course_full_name: data.full_name,
      course_short_name: data.short_name,
      organization_id: data.organization_id
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(body)
    }
    const url = 'http://localhost:8080/courses/stage'
    fetcher(url, options)
      .then(json => {
        const newItem = {
          fullName: data.full_name,
          shortName: data.short_name,
          organizationId: data.organization_id,
          votes: json.votes,
          timestamp: json.timestamp,
          stagedId: json.stagedId,
          createdBy: json.createdBy
        }
        message.success('Successfully created course')
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem],
          createStagedFlag: false
        }))
      })
      .catch(error => {
        message.error('Successfully created course')
        this.setState({ progError: error, createStagedFlag: false })
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const courseId = this.state.courseID
    const url = `http://localhost:8080/courses/${courseId}/vote`
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
        let newArray = [...prevState.courses]
        const index = newArray.findIndex(course => course.courseId === courseId)
        newArray[index].votes = prevState.courses[index].votes + 1
        message.success('Vote Up!!!')
        return ({
          courses: newArray,
          voteUp: false
        })
      }))
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteUp: false})
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const courseId = this.state.courseID
    const url = `http://localhost:8080/courses/${courseId}/vote`
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
        let newArray = [...prevState.courses]
        const index = newArray.findIndex(course => course.courseId === courseId)
        newArray[index].votes = prevState.courses[index].votes - 1
        message.success('Vote Down!!!')
        return ({
          courses: newArray,
          voteDown: false
        })
      }))
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteDown: false})
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `http://localhost:8080/courses/stage/${stageID}/vote`
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
      .then(_ => {
        message.success('Voted staged up!!!')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(course => course.stageId === stageID)
          newArray[index].votes = prevState.staged[index].votes + 1
          return ({
            staged: newArray,
            voteUpStaged: false
          })
        })
      })
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteUpStaged: false})
      })
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Down'
    }
    const stageID = this.state.stageID
    const url = `http://localhost:8080/courses/stage/${stageID}/vote`
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
      .then(_ => {
        message.success('Voted staged up!!!')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(course => course.stageId === stageID)
          newArray[index].votes = prevState.staged[index].votes - 1
          return ({
            staged: newArray,
            voteDownStaged: false
          })
        })
      })
      .catch(_ => {
        message.error('Error processing your vote')
        this.setState({voteDownStaged: false})
      })
  }

  componentDidUpdate () {
    if (this.state.createStagedFlag) {
      this.createStagedCourse()
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUpStaged) {
      this.voteUpStaged()
    } else if (this.state.voteDownStaged) {
      this.voteDownStaged()
    }
  }
}

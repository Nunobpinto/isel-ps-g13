import React from 'react'
import fetch from 'isomorphic-fetch'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import CreateCourse from './CreateCourse'
import { Button, Input, message, List, Card } from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courses: [],
      viewCourses: [],
      error: undefined,
      voteUp: false,
      voteDown: false,
      staged: [],
      viewStaged: [],
      nameFilter: '',
      stagedNameFilter: ''
    }
    this.showElements = this.showElements.bind(this)
    this.createCourseForm = this.createCourseForm.bind(this)
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.createStagedCourse = this.createStagedCourse.bind(this)
    this.filterCoursesByName = this.filterCoursesByName.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
  }

  showElements (id) {
    const element = document.getElementById(id)
    element.className = 'show_staged_resources'
  }

  filterCoursesByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = prevState.courses
      array = array.filter(course => course.fullName.toLowerCase().includes(name))
      return ({viewCourses: array})
    })
  }

  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = prevState.staged
      array = array.filter(staged => staged.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    this.setState({
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      organization_id: 1,
      createStagedFlag: true
    })
  }

  render () {
    return (
      <Layout>
        <div className='container'>
          <div className='left-div'>
            {this.state.error
              ? <p> Error getting all the courses please try again !!! </p>
              : <div>
                <h1>All courses in ISEL</h1>
                <p> Filter By Name </p>
                <Input.Search
                  name='nameFilter'
                  placeholder='Search name'
                  onChange={this.filterCoursesByName}
                />
                <List
                  itemLayout='vertical'
                  size='large'
                  bordered
                  dataSource={this.state.viewCourses}
                  renderItem={item => (
                    <List.Item
                      actions={[
                        <IconText
                          type='like-o'
                          id='like_btn'
                          onClick={() =>
                            this.setState({
                              voteUp: true,
                              courseID: item.courseId
                            })}
                          text={item.votes}
                        />,
                        <IconText
                          type='dislike-o'
                          id='dislike_btn'
                          onClick={() =>
                            this.setState({
                              voteDown: true,
                              courseID: item.courseId
                            })}
                        />
                      ]}
                    >
                      <List.Item.Meta
                        title={<Link to={{ pathname: `/courses/${item.courseId}` }}> {item.fullName} ({item.shortName})</Link>}
                        description={`Created by ${item.createdBy}`}
                      />
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
                            stageID: item.stageId
                          })}
                        text={item.votes}
                      />
                      <IconText
                        type='dislike-o'
                        id='dislike_btn'
                        onClick={() =>
                          this.setState({
                            voteDownStaged: true,
                            stageID: item.stageId
                          })}
                      />
                    </List.Item>
                  )}
                />
                <Button type='primary' onClick={() => this.setState({createCourseForm: true})}>Still want to create?</Button>
                {this.state.createCourseForm && <CreateCourse action={this.createStagedCourse} />}
              </div>
            }
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/courses/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(courses => {
        const stagedUri = 'http://localhost:8080/courses/stage'
        fetch(stagedUri, header)
          .then(response => {
            if (response.status >= 400) {
              throw new Error('Unable to access content')
            }
            const ct = response.headers.get('content-type') || ''
            if (ct === 'application/json' || ct.startsWith('application/json;')) {
              return response.json()
            }
            throw new Error(`unexpected content type ${ct}`)
          })
          .then(stagedCourses => this.setState({
            courses: courses.courseList,
            viewCourses: courses.courseList,
            staged: stagedCourses.courseStageList,
            viewStaged: stagedCourses.courseStageList
          }))
          .catch(stagedError => this.setState({
            courses: courses.courseList,
            viewCourses: courses.courseList,
            stagedError: stagedError
          }))
      })
      .catch(error => {
        this.setState({
          error: error
        })
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
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(body)
    }
    const url = 'http://localhost:8080/courses/stage'
    fetch(url, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        return resp.json()
      })
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
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.courses]
          const index = newArray.findIndex(course => course.courseId === courseId)
          newArray[index].votes = prevState.courses[index].votes + 1
          return ({
            courses: newArray,
            voteUp: false
          })
        })
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
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.courses]
          const index = newArray.findIndex(course => course.courseId === courseId)
          newArray[index].votes = prevState.courses[index].votes - 1
          return ({
            courses: newArray,
            voteDown: false
          })
        })
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
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        message.success('Successfully voted up')
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
      .catch(_ => message.error('Cannot vote'))
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
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
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
      .catch(_ => message.error('Cannot vote'))
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

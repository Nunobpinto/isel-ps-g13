import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import CreateCourse from './CreateCourse'
import { Button, Input, message, List, Card } from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default (props) => (
  <Layout>
    <Courses />
  </Layout>
)

class Courses extends React.Component {
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
    this.handleChange = this.handleChange.bind(this)
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedCourse = this.createStagedCourse.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createDefinitiveCourse = this.createDefinitiveCourse.bind(this)
    this.filterCoursesByName = this.filterCoursesByName.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
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

  render () {
    return (
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
          {this.props.user.reputation.role === 'ROLE_ADMIN'
            ? <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({createCourseView: true})}>Create Course</Button>
            : <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({stagedCourseView: true})}>Create Course</Button>
          }
        </div>
        <div className='right-div'>
          {this.state.createCourseView &&
          <CreateCourse action={this.createDefinitiveCourse} />
          }
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
                {this.state.createCourseForm && <CreateCourse action={(data) => this.createStagedCourse(data)} />}
              </div>
          }
        </div>
      </div>
    )
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/courses/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(courses => {
        const stagedUri = 'http://localhost:8080/courses/stage'
        fetcher(stagedUri, header)
          .then(stagedCourses => this.setState({
            courses: courses.courseList,
            viewCourses: courses.courseList,
            staged: stagedCourses.courseStageList,
            viewStaged: stagedCourses.courseStageList
          }))
          .catch(stagedError => {
            message.error('Error fetching staged courses')
            this.setState({
              courses: courses.courseList,
              viewCourses: courses.courseList,
              stagedError: stagedError
            })
          })
      })
      .catch(error => {
        message.error('Error fetching courses')
        this.setState({error: error})
      })
  }

  createStagedCourse (data) {
    const body = {
      course_full_name: data.full_name,
      course_short_name: data.short_name
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
          votes: json.votes,
          timestamp: json.timestamp,
          stagedId: json.stagedId,
          createdBy: json.createdBy
        }
        message.success('Successfully created staged course')
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem]
        }))
      })
      .catch(_ => {
        message.error('Error creating staged course')
      })
  }

  createDefinitiveCourse (data) {
    const body = {
      course_full_name: data.full_name,
      course_short_name: data.short_name
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
    const url = 'http://localhost:8080/courses'
    fetcher(url, options)
      .then(json => {
        const newItem = {
          fullName: data.full_name,
          shortName: data.short_name,
          votes: json.votes,
          timestamp: json.timestamp,
          courseId: json.courseId,
          createdBy: json.createdBy
        }
        message.success('Successfully created course')
        this.setState(prevState => ({
          courses: [...prevState.courses, newItem],
          viewCourses: [...prevState.viewCourses, newItem]
        }))
      })
      .catch(_ => {
        message.error('Error creating course')
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
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
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
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote down')
        }
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
          const index = newArray.findIndex(course => course.stagedId === stageID)
          newArray[index].votes = prevState.staged[index].votes + 1
          return ({
            staged: newArray,
            voteUpStaged: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
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
          const index = newArray.findIndex(course => course.stagedId === stageID)
          newArray[index].votes = prevState.staged[index].votes - 1
          return ({
            staged: newArray,
            voteDownStaged: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteDownStaged: false})
      })
  }

  componentDidUpdate () {
    if (this.state.voteUp) {
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

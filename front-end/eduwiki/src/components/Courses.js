import React from 'react'
import fetch from 'isomorphic-fetch'
import { Link } from 'react-router-dom'
import IconText from './IconText'
import Layout from './Layout'
import { Button, Input, Form, List, Card } from 'antd'

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
      created_by: this.state.created_by,
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
                <Input
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
                              courseID: item.id
                            })}
                          text={item.votes}
                        />,
                        <IconText
                          type='dislike-o'
                          id='dislike_btn'
                          onClick={() =>
                            this.setState({
                              voteDown: true,
                              courseID: item.id
                            })}
                        />
                      ]}
                    >
                      <List.Item.Meta
                        title={<Link to={{ pathname: `/courses/${item.id}` }}> {item.fullName} ({item.shortName})</Link>}
                        description={`Created by ${item.createdBy}`}
                      />
                    </List.Item>
                  )}
                />
              </div>
            }
            <Button icon='plus' id='create_btn' type='primary' onClick={() => { this.showElements('stagedCourses') }}>Create Course</Button>
          </div>
          <div class='right-div'>
            <div id='stagedCourses' class='hide_staged_resources'>
              <h1>All staged Courses</h1>
              <p> Filter By Name : </p>
              <Input
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
                  </List.Item>
                )}
              />
              <Button type='primary' onClick={() => { this.showElements('formToCreateCourse') }}>Still want to create?</Button>
            </div>
            {this.createCourseForm()}
          </div>
        </div>
      </Layout>
    )
  }

  createCourseForm () {
    return (
      <div id='formToCreateCourse' class='hide_staged_resources'>
        <Form>
          Full name: <br />
          <Input name='full_name' onChange={this.handleChange} />
          <br />
          Short name: <br />
          <Input name='short_name' onChange={this.handleChange} />
          <br />
          Created By: <br />
          <Input name='created_by' onChange={this.handleChange} />
          <br />
          <Button type='primary' onClick={this.handleSubmit}>Create</Button>
        </Form>
      </div>
    )
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/courses/'
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
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
            courses: courses,
            viewCourses: courses,
            staged: stagedCourses,
            viewStaged: stagedCourses
          }))
          .catch(stagedError => this.setState({
            courses: courses,
            viewCourses: courses,
            stagedError: stagedError
          }))
      })
      .catch(error => {
        this.setState({
          error: error
        })
      })
  }

  createStagedCourse () {
    const data = {
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      created_by: this.state.created_by,
      organization_id: 1
    }
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }
    const url = 'http://localhost:8080/courses/stage'
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const newItem = {
          fullName: data.full_name,
          shortName: data.short_name,
          createdBy: data.created_by,
          organizationId: 1
        }
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem],
          createStagedFlag: false
        }))
      })
      .catch(error => {
        this.setState({ progError: error, createStagedFlag: false })
      })
  }

  voteUp () {

  }

  voteDown () {

  }

  voteUpStaged () {

  }

  voteDownStaged () {

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

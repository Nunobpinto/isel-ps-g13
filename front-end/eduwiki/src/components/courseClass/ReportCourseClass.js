import React from 'react'
import fetcher from '../../fetcher'
import {Form, Button, message, Radio} from 'antd'
import RadioGroup from 'antd/lib/radio/group'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      course_id: undefined,
      courses: [],
      deleteFlag: undefined,
      reported: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }
  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    const data = {
      course_id: this.props.courseId,
      delete_permanently: this.state.deleteFlag,
      class_id: this.props.classId,
      term_id: this.props.termId
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    return (
      <div>
        <h1>Choose other Course for this Class or choose to report the whole association between this course and this class</h1>
        <Form>
        Delete Association: <br />
          <RadioGroup name='deleteFlag' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </RadioGroup>
          <br />
          {/*
          Courses: <br />
          <RadioGroup name='course_id' onChange={this.handleChange}>
            {this.state.courses.map(crs => (
              <Radio value={crs.courseId}>{crs.shortName}</Radio>
            ))}
          </RadioGroup>
          <br />
           */}
          <Button type='primary' onClick={this.handleSubmit}>Create</Button>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.reported) {
      let data = this.state.data
      const properties = Object.keys(data)
      properties.forEach(prop => {
        if (!data[prop]) {
          delete data[prop]
        }
      })
      delete data.reported
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
          'tenant-uuid': config.TENANT_UUID
        },
        body: JSON.stringify(data)
      }
      const url = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/reports`
      fetcher(url, options)
        .then(_ => {
          message.success('Reported!!')
          this.setState({
            reported: false
          })
        })
        .catch(_ => {
          message.error('Error processing your report')
          this.setState({reported: false})
        })
    }
  }
  componentDidMount () {
    const classUrl = `${config.API_PATH}/classes/${this.props.classId}`
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(classUrl, options)
      .then(klass => {
        const url = `${config.API_PATH}/programmes/${klass.programmeId}/courses`
        fetcher(url, options)
          .then(json => {
            let courses = json.courseProgrammeList
            courses = courses.filter(crs => crs.courseId !== Number(this.props.courseId))
            this.setState({
              courses: courses
            })
          })
          .catch(_ => message.error('Error loading courses'))
      })
      .catch(_ => message.error('Error loading class'))
  }
}

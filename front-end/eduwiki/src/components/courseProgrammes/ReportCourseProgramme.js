import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message, Radio} from 'antd'
import RadioGroup from 'antd/lib/radio/group'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      course_lectured_term: undefined,
      credits: undefined,
      optional: undefined,
      to_delete: undefined,
      programmes: [],
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
      course_lectured_term: this.state.course_lectured_term,
      to_delete: this.state.to_delete,
      optional: this.state.optional,
      credits: this.state.credits
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    return (
      <div>
        <h1>Report the fields that you don't agree or choose to report the whole association between this course and this programme</h1>
        <Form>
        Delete Association: <br />
          <RadioGroup name='to_delete' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </RadioGroup>
          <br />
        Lecture term: <br />
          <Input name='course_lectured_term' onChange={this.handleChange} />
          <br />
        Optional: <br />
          <RadioGroup name='optional' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </RadioGroup>
          <br />
        Credits: <br />
          <input type='number' name='credits' onChange={this.handleChange} />
          <br />
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
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: JSON.stringify(data)
      }
      const url = `http://localhost:8080/programmes/${this.props.programmeId}/courses/${this.props.courseId}/reports`
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
}

import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message, Radio, DatePicker} from 'antd'
import moment from 'moment'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      due_date: undefined,
      type: undefined,
      phase: undefined,
      location: undefined,
      reported: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleDateChange = this.handleDateChange.bind(this)
  }
  handleDateChange (date, dateString) {
    this.setState({
      due_date: dateString
    })
  }
  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    let date
    if (this.state.due_date) {
      date = this.state.due_date.format('YYYY-MM-DD')
    }
    const data = {
      due_date: date,
      individual: this.state.individual,
      late_delivery: this.state.lateDelivery,
      multiple_deliveries: this.state.multipleDeliveries,
      requires_report: this.state.requiresReport,
      phase: this.state.phase
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    const dateFormat = 'YYYY-MM-DD'
    return (
      <div>
        <h1>Report the fields that you don't agree</h1>
        <Form>
        Phase: <br />
          <Input name='phase' onChange={this.handleChange} />
          <br />
          Individual Work Assignment: <br />
          <Radio.Group name='individual' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </Radio.Group>
          <br />
          Late Delivery: <br />
          <Radio.Group name='lateDelivery' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </Radio.Group>
          <br />
          Multiple Deliveries: <br />
          <Radio.Group name='multipleDeliveries' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </Radio.Group>
          <br />
          Requires Report: <br />
          <Radio.Group name='requiresReport' onChange={this.handleChange}>
            <Radio value='true'>Yes</Radio>
            <Radio value='false'>No</Radio>
          </Radio.Group>
          <br />
        Due Date: <br />
          <DatePicker
            name='due_date'
            defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
            format={dateFormat}
            onChange={this.handleDateChange}
          />
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
          'tenant-uuid': config.TENANT_UUID
        },
        body: JSON.stringify(data)
      }
      const url = `${config.API_PATH}/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments/${this.props.workAssignmentId}/reports`
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

import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message, InputNumber, Select, TimePicker} from 'antd'
import moment from 'moment'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      weekDay: undefined,
      begins: undefined,
      duration: undefined,
      location: undefined,
      reported: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleTimeChange = this.handleTimeChange.bind(this)
  }
  handleTimeChange (time, timeString) {
    this.setState({
      begins: timeString
    })
  }
  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }
  handleSubmit (ev) {
    ev.preventDefault()
    const data = {
      weekDay: this.state.weekDay,
      begins: this.state.begins,
      location: this.state.location,
      duration: this.state.duration
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    const timeFormat = 'HH:mm'
    return (
      <div>
        <h1>Report the fields that you don't agree</h1>
        <Form>
          Week Day :  <br />
          <Select
            showSearch
            style={{ width: 200 }}
            placeholder='Select a weekday'
            optionFilterProp='children'
            name='weekDay'
            onChange={this.handleChange}
          >
            <Select.Option value='MONDAY'>Monday</Select.Option>
            <Select.Option value='TUESDAY'>Tuesday</Select.Option>
            <Select.Option value='WEDNESDAY'>Wednesday</Select.Option>
            <Select.Option value='THURSDAY'>Thursday</Select.Option>
            <Select.Option value='FRIDAY'>Friday</Select.Option>
            <Select.Option value='SATURDAY'>Saturday</Select.Option>
          </Select>
          <br />
          Begins at : <br /><TimePicker name='begins' defaultValue={moment('12:08', timeFormat)} format={timeFormat} onChange={this.handleTimeChange} />
          <br />
        Loction: <br />
          <Input name='location' onChange={this.onChange} />
          <br />
        Duration in Hours: <br />
          <InputNumber name='duration' onChange={this.onChange} min={1} />
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
      const url = `${config.API_PATH}/classes/${this.props.classId}/courses/${this.props.courseId}/lectures/${this.props.lectureId}/reports`
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

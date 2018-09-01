import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message, Radio, DatePicker} from 'antd'
import Cookies from 'universal-cookie'
import moment from 'moment'
const cookies = new Cookies()

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
      exam_type: this.state.type,
      phase: this.state.phase,
      location: this.state.location
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
        Phase: <br />
          <Input name='location' onChange={this.handleChange} />
          <br />
        Type: <br />
          <Radio.Group name='type' onChange={this.handleChange}>
            <Radio value='TEST'>Test</Radio>
            <Radio value='EXAM'>Exam</Radio>
          </Radio.Group>
          <br />
        Due Date: <br />
          <DatePicker
            name='due_date'
            defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
            format={dateFormat}
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
          'Authorization': 'Basic ' + cookies.get('auth'),
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: JSON.stringify(data)
      }
      const url = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/exams/${this.props.examId}/reports`
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
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    const uri = 'http://localhost:8080/programmes'
    fetcher(uri, options)
      .then(json => {
        let id = Number(this.props.programmeId)
        let programmes = json.programmeList
        programmes = programmes.filter(prog => prog.programmeId !== id)
        this.setState({programmes: programmes})
      })
      .catch(_ => message.error('Error fetching programmes '))
  }
}

import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: undefined,
      short_name: undefined,
      academic_degree: undefined,
      total_credits: undefined,
      duration: undefined,
      programmeID: props.id
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
      programme_full_name: this.state.full_name,
      programme_short_name: this.state.short_name,
      programme_academic_degree: this.state.academic_degree,
      programme_total_credits: this.state.total_credits,
      programme_duration: this.state.duration
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    return (
      <div>
        <h1>Report the fields that you don't agree</h1>
        <Form>
        Full name: <br />
          <Input name='full_name' onChange={this.handleChange} />
          <br />
        Short name: <br />
          <Input name='short_name' onChange={this.handleChange} />
          <br />
        Academic Degree: <br />
          <Input name='academic_degree' onChange={this.handleChange} />
          <br />
        Total Credits: <br />
          <input type='number' name='total_credits' onChange={this.handleChange} />
          <br />
        Duration: <br />
          <input type='number' name='duration' onChange={this.handleChange} />
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
      const url = `http://localhost:8080/programmes/${this.props.id}/reports`
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

import React from 'react'
import fetch from 'isomorphic-fetch'
import {Input, Form, Button} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: undefined,
      short_name: undefined,
      academic_degree: undefined,
      total_credits: undefined,
      duration: undefined,
      reported_by: undefined,
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
    this.setState({
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      academic_degree: this.state.academic_degree,
      total_credits: this.state.total_credits,
      duration: this.state.duration,
      reported_by: this.state.reported_by,
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
        Reported By: <br />
          <Input name='reported_by' onChange={this.handleChange} />
          <br />
          <Button type='primary' onClick={this.handleSubmit}>Create</Button>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.reported) {
      let data = this.state
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
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      }
      const url = `http://localhost:8080/programmes/${this.props.id}/reports`
      fetch(url, options)
        .then(resp => {
          if (resp.status < 400) {
            this.props.history.push('/programmes/' + this.props.id)
          }
        })
    }
  }
}

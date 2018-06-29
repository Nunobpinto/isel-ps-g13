import React from 'react'
import fetch from 'isomorphic-fetch'
import {Input, Form, Button} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: undefined,
      short_name: undefined,
      address: undefined,
      contact: undefined,
      reported_by: undefined,
      organizationId: props.id,
      history: props.history
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
      address: this.state.address,
      contact: this.state.contact,
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
        Address: <br />
          <Input name='address' onChange={this.handleChange} />
          <br />
        Contact: <br />
          <Input name='contact' onChange={this.handleChange} />
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
      const url = `http://localhost:8080/organizations/${this.props.id}/reports`
      fetch(url, options)
        .then(resp => {
          if (resp.status < 400) {
            this.state.history.push('/programmes/' + this.props.id)
          }
        })
    }
  }
}

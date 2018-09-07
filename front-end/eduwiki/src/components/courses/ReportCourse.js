import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message} from 'antd'
import config from '../../config'

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
      course_full_name: this.state.full_name,
      course_short_name: this.state.short_name
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
      const url = `${config.API_PATH}/courses/${this.props.id}/reports`
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

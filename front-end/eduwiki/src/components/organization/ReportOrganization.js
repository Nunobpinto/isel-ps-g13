import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message} from 'antd'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization_full_name: undefined,
      organization_short_name: undefined,
      organization_address: undefined,
      organization_contact: undefined,
      organization_website: undefined,
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
      reported: true
    })
  }
  render () {
    return (
      <div>
        <h1>Report the fields that you don't agree</h1>
        <Form>
        Full name: <br />
          <Input name='organization_full_name' onChange={this.handleChange} />
          <br />
        Short name: <br />
          <Input name='organization_short_name' onChange={this.handleChange} />
          <br />
        Address: <br />
          <Input name='organization_address' onChange={this.handleChange} />
          <br />
        Contact: <br />
          <Input name='organization_contact' onChange={this.handleChange} />
          <br />
          Website: <br />
          <Input name='organization_website' onChange={this.handleChange} />
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
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
          'tenant-uuid': config.TENANT_UUID
        },
        body: JSON.stringify(data)
      }
      const url = `${config.API_PATH}/organization/reports`
      fetcher(url, options)
        .then(_ => {
          message.success('Reported !!')
          this.setState({reported: false})
        })
        .catch(_ => {
          message.error('Error while processing your report!!')
          this.setState({reported: false})
        })
    }
  }
}

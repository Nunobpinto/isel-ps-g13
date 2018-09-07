import React from 'react'
import fetcher from '../../fetcher'
import {Input, Form, Button, message, Radio} from 'antd'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      class_name: undefined,
      programme_id: undefined,
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
      class_name: this.state.class_name,
      programme_id: this.state.programme_id
    }
    this.setState({
      data: data,
      reported: true
    })
  }
  render () {
    return (
      <div>
        <h1>Report the fields that you don't agree </h1>
        <Form>
        Class Name: <br />
          <Input name='class_name' onChange={this.handleChange} />
          <br />
        Programme: <br />
          <Radio.Group name='programme_id' onChange={this.handleChange}>
            {this.state.programmes.map(prog => (
              <Radio value={prog.programmeId}>{prog.shortName}</Radio>
            ))}
          </Radio.Group>
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
      const url = `${config.API_PATH}/classes/${this.props.classId}/reports`
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
    const url = config.API_PATH + '/programmes'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, options)
      .then(json => {
        let programmes = json.programmeList
        programmes = programmes.filter(prog => prog.programmeId !== this.props.programmeId)
        this.setState({programmes: programmes})
      })
  }
}

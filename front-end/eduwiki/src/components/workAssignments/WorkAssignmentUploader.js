import React from 'react'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
import {message} from 'antd'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      file: undefined,
      supplement: undefined
    }
    this.handleFileChange = this.handleFileChange.bind(this)
    this.handleSupChange = this.handleSupChange.bind(this)
  }
  handleFileChange (e) {
    this.setState({file: e.target.files[0]})
  }
  handleSupChange (e) {
    this.setState({supplement: e.target.files[0]})
  }
  render () {
    return (
      <div>
        <label for='file'>Choose file to upload</label>
        <input id='file' type='file' name='sheet' onChange={this.handleFileChange} />
        <br />
        <label for='supplement'>Choose supplement to upload</label>
        <input id='supplement' type='file' name='supplement' onChange={this.handleSupChange} />
        <br />
        <button onClick={() => this.setState({submitted: true})}>Submit></button>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.file && this.state.submitted) {
      const uri = `http://localhost:8080/courses/${this.props.courseId}/terms/${this.props.termId}/work-assignments`
      let data = new FormData()
      data.append('sheet', this.state.file)
      data.append('supplement', this.state.supplement)
      data.append('phase', this.props.data.phase)
      data.append('dueDate', this.props.data.dueDate)
      data.append('individual', this.props.data.individual)
      data.append('multipleDeliveries', this.props.data.multipleDeliveries)
      data.append('lateDelivery', this.props.data.lateDelivery)
      data.append('requiresReport', this.props.data.requiresReport)
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Basic ' + cookies.get('auth'),
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: data
      }
      fetcher(uri, options)
        .then(_ => {
          message.success('Saved the Work Assignment you requested')
          this.setState({file: undefined})
        })
        .catch(_ => {
          message.error('Error while saving the Work Assignment you requested')
          this.setState({file: undefined})
        })
    }
  }
}

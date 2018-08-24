import React from 'react'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
import {message} from 'antd'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      file: undefined
    }
    this.handleChange = this.handleChange.bind(this)
  }

  handleChange (e) {
    this.setState({file: e.target.files[0]})
  }
  render () {
    return (
      <div>
        <label for='file'>Choose file to upload</label>
        <input id='file' type='file' name='sheet' onChange={this.handleChange} />
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.file) {
      const uri = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.courseId}/homeworks`
      let data = new FormData()
      data.append('sheet', this.state.file)
      data.append('dueDate', this.props.data.dueDate)
      data.append('multipleDeliveries', this.props.data.multipleDeliveries)
      data.append('lateDelivery', this.props.data.lateDelivery)
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
          message.success('Saved the homework you registed')
          this.setState({file: undefined})
        })
        .catch(_ => {
          message.error('Error while saving the exam you registed')
          this.setState({file: undefined})
        })
    }
  }
}

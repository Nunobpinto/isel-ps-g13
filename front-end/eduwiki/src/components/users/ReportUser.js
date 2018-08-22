import { Form, Input, Button, message } from 'antd'
import React from 'react'
import Cookies from 'universal-cookie'
import fetcher from '../../fetcher'
const cookies = new Cookies()

const FormItem = Form.Item
const { TextArea } = Input

class ReportForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      reported: false
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, value) => {
      if (!err) {
        this.setState({
          reported: true,
          reason: value.reason
        })
      }
    })
  }
  render () {
    const { getFieldDecorator } = this.props.form
    return (
      <Form onSubmit={this.handleSubmit}>
        <FormItem
          label='Reason to report User'
        >
          {getFieldDecorator('reason', {
            rules: [{
              required: true, message: 'Please describe a valid reason'
            }]
          })(
            <TextArea rows={4} />
          )}
        </FormItem>
        <FormItem>
          <Button type='primary' htmlType='submit'>Report</Button>
        </FormItem>
      </Form>
    )
  }
  componentDidUpdate () {
    if (this.state.reported) {
      const report = {
        reason: this.state.reason
      }
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + cookies.get('auth'),
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: JSON.stringify(report)
      }
      this.props.form.resetFields()
      fetcher(`http://localhost:8080/users/${this.props.username}/report`, options)
        .then(_ => {
          message.success('Successfully reported')
          this.setState({
            reported: false
          })
        })
        .catch(_ => {
          message.error('Failure in processing the report, please try again later')
          this.setState({
            reported: false
          })
        })
    }
  }
}

export default Form.create()(ReportForm)

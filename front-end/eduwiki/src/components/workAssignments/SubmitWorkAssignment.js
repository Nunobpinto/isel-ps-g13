import React from 'react'
import { Form, Input, Button, DatePicker, Radio } from 'antd'
import moment from 'moment'

const FormItem = Form.Item
const RadioGroup = Radio.Group

class SubmitWorkAssignment extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      dueDate: undefined,
      individual: false,
      lateDelivery: false,
      multipleDeliveries: false,
      uploadFlag: '',
      requiresReport: false,
      data: undefined,
      sheet: undefined,
      supplement: undefined
    }
    this.handleFileChange = this.handleFileChange.bind(this)
    this.handleSupChange = this.handleSupChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }
  handleFileChange (e) {
    this.setState({sheet: e.target.files[0]})
  }
  handleSupChange (e) {
    this.setState({supplement: e.target.files[0]})
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.dueDate = values.dueDate.format('YYYY-MM-DD')
        values.sheet = this.state.sheet
        values.supplement = this.state.supplement
        this.setState({
          uploadFlag: true,
          data: values
        })
      }
    })
  }
  render () {
    const { getFieldDecorator } = this.props.form
    const dateFormat = 'YYYY-MM-DD'
    return (
      <div>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            label='Due Date'
          >
            {getFieldDecorator('dueDate', {
              rules: [{
                required: true, message: 'Please add a Due Date to the new Work Assignment'
              }]
            })(
              <DatePicker
                defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
                format={dateFormat}
              />
            )}
          </FormItem>
          <FormItem
            label='Individual Work Assignment'
          >
            {getFieldDecorator('individual', {
              rules: [{
                required: true, message: 'Please choose if the work is individual or not'
              }]
            })(
              <RadioGroup defaultValue='true'>
                <Radio value='true' checked>Yes</Radio>
                <Radio value='false'>No</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem
            label='Late Delivery'
          >
            {getFieldDecorator('lateDelivery', {
              rules: [{
                required: true, message: 'Please choose if the work has late delivery or not'
              }]
            })(
              <RadioGroup defaultValue='true'>
                <Radio value='true' checked>Yes</Radio>
                <Radio value='false'>No</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem
            label='Multiple Deliveries'
          >
            {getFieldDecorator('multipleDeliveries', {
              rules: [{
                required: true, message: 'Please choose if the work has multiple deliveries or not'
              }]
            })(
              <RadioGroup defaultValue='true'>
                <Radio value='true' checked>Yes</Radio>
                <Radio value='false'>No</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem
            label='Requires Report'
          >
            {getFieldDecorator('requiresReport', {
              rules: [{
                required: true, message: 'Please choose if the work requires report or not'
              }]
            })(
              <RadioGroup defaultValue='true'>
                <Radio value='true' checked>Yes</Radio>
                <Radio value='false'>No</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem
            label='Phase'
          >
            {getFieldDecorator('phase', {
              rules: [{
                required: true, message: 'Please add a Phase to the new Work Assignment'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Sheet'
          >
            <input id='file' type='file' name='sheet' onChange={this.handleFileChange} />
          </FormItem>
          <FormItem
            label='Sheet'
          >
            <input id='supplement' type='file' name='supplement' onChange={this.handleSupChange} />
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Work Assignment</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.uploadFlag) {
      this.props.action(this.state.data)
      this.setState({uploadFlag: false})
    }
  }
}

export default Form.create()(SubmitWorkAssignment)

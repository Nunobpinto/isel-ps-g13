import React from 'react'
import { Form, Input, Button, DatePicker, Radio, message } from 'antd'
import moment from 'moment'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const FormItem = Form.Item
const RadioGroup = Radio.Group

class SubmitExam extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      dueDate: undefined,
      lateDelivery: false,
      multipleDeliveries: false,
      uploadFlag: false,
      file: undefined,
      data: undefined,
      homeworkName: undefined
    }
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleChange = this.handleChange.bind(this)
  }
  handleChange (e) {
    this.setState({file: e.target.files[0]})
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.dueDate = values.dueDate.format('YYYY-MM-DD')
        values.file = this.state.file
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
            label='Name'
          >
            {getFieldDecorator('homeworkName', {
              rules: [{
                required: true, message: 'Please write a name for the homework'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Due Date'
          >
            {getFieldDecorator('dueDate', {
              rules: [{
                required: true, message: 'Please add a Due Date to the new Homework'
              }]
            })(
              <DatePicker
                defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
                format={dateFormat}
              />
            )}
          </FormItem>
          <FormItem
            label='Late Delivery'
          >
            {getFieldDecorator('lateDelivery', {
              rules: [{
                required: true, message: 'Please choose if the homework has late delivery or not'
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
                required: true, message: 'Please choose if the homework has multiple deliveries or not'
              }]
            })(
              <RadioGroup defaultValue='true'>
                <Radio value='true' checked>Yes</Radio>
                <Radio value='false'>No</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem
            label='Homework File'
          >
            <input id='file' type='file' name='sheet' onChange={this.handleChange} />
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Homework</Button>
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

export default Form.create()(SubmitExam)

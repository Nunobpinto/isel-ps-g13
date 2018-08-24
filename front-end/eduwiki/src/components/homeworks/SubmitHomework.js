import React from 'react'
import { Form, Input, Button, DatePicker, Select, Radio } from 'antd'
import HomeworkUploader from './HomeworkUploader'
import moment from 'moment'

const FormItem = Form.Item
const RadioGroup = Radio.Group

class SubmitWorkAssignment extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      dueDate: undefined,
      lateDelivery: false,
      multipleDeliveries: false,
      uploadFlag: '',
      data: undefined
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.dueDate = values.dueDate.format('YYYY-MM-DD')
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
          <FormItem>
            <Button type='primary' htmlType='submit'>Create WorkAssignment</Button>
          </FormItem>
        </Form>
        {this.state.uploadFlag &&
        <HomeworkUploader
          data={this.state.data}
          courseId={this.props.courseId}
          classId={this.props.classId}
        />
        }
      </div>
    )
  }
}

export default Form.create()(SubmitWorkAssignment)

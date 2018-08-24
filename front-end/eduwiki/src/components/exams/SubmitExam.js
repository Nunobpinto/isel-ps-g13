import React from 'react'
import { Form, Input, Button, DatePicker, Select } from 'antd'
import ExamUploader from './ExamUploader'
import moment from 'moment'

const FormItem = Form.Item

class SubmitExam extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      dueDate: undefined,
      type: '',
      phase: '',
      location: '',
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
            label='Completion Date'
          >
            {getFieldDecorator('dueDate', {
              rules: [{
                required: true, message: 'Please add a Due Date to the new Exam'
              }]
            })(
              <DatePicker
                defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
                format={dateFormat}
              />
            )}
          </FormItem>
          <FormItem
            label='Type'
          >
            {getFieldDecorator('type', {
              rules: [{
                required: true, message: 'Please add a Type to the new Exam'
              }]
            })(
              <Select
                showSearch
                style={{ width: 200 }}
                placeholder='Select a type'
                optionFilterProp='children'
              >
                <Select.Option value='Exam'>Exam</Select.Option>
                <Select.Option value='Test'>Test</Select.Option>
              </Select>
            )}
          </FormItem>
          <FormItem
            label='Phase'
          >
            {getFieldDecorator('phase', {
              rules: [{
                required: true, message: 'Please add a Phase to the new Exam'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Location'
          >
            {getFieldDecorator('location', {
              rules: [{
                required: true, message: 'Please add a Location to the new Exam'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Exam</Button>
          </FormItem>
        </Form>
        {this.state.uploadFlag &&
        <ExamUploader
          data={this.state.data}
          courseId={this.props.courseId}
          termId={this.props.termId}
        />
        }
      </div>
    )
  }
}

export default Form.create()(SubmitExam)

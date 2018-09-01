import React from 'react'
import { Form, Input, Button, DatePicker, Select } from 'antd'
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
            label='Due Date'
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
          <FormItem
            label='File'
          >
            <input id='file' type='file' name='sheet' onChange={this.handleChange} />
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Exam</Button>
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

import React from 'react'
import { Form, Input, Button, InputNumber, Select, TimePicker, message } from 'antd'
import moment from 'moment'
import fetcher from '../../fetcher'

const FormItem = Form.Item

class SubmitExam extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      dueDate: undefined,
      type: '',
      phase: '',
      location: '',
      createFlag: '',
      data: undefined
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.setState({
          createFlag: true,
          data: values
        })
      }
    })
  }
  render () {
    const { getFieldDecorator } = this.props.form
    const timeFormat = 'HH:mm'
    return (
      <div>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            label='Week Day'
          >
            {getFieldDecorator('weekDay', {
              rules: [{
                required: true, message: 'Please choose day of The Week to the new Lecture'
              }]
            })(
              <Select
                showSearch
                style={{ width: 200 }}
                placeholder='Select a weekday'
                optionFilterProp='children'
              >
                <Select.Option value='MONDAY'>Monday</Select.Option>
                <Select.Option value='TUESDAY'>Tuesday</Select.Option>
                <Select.Option value='WEDNESDAY'>Wednesday</Select.Option>
                <Select.Option value='THURSDAY'>Thursday</Select.Option>
                <Select.Option value='FRIDAY'>Friday</Select.Option>
                <Select.Option value='SATURDAY'>Saturday</Select.Option>
              </Select>
            )}
          </FormItem>
          <FormItem
            label='Begins'
          >
            {getFieldDecorator('begins', {
              rules: [{
                required: true, message: 'Please choose beginning time of the new Lecture'
              }]
            })(
              <TimePicker defaultValue={moment('12:08', timeFormat)} format={timeFormat} />
            )}
          </FormItem>
          <FormItem
            label='Location'
          >
            {getFieldDecorator('location', {
              rules: [{
                required: true, message: 'Please add a Location to the new Lecture'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Duration In Hours'
          >
            {getFieldDecorator('duration', {
              rules: [{
                required: true, message: 'Please add Duration to the new Lecture'
              }]
            })(
              <InputNumber min={1} />
            )}
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Lecture</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.createFlag) {
      const uri = `http://localhost:8080/classes/${this.props.classId}/courses/${this.props.courseId}/lectures`
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: JSON.stringify(this.state.data)
      }
      fetcher(uri, options)
        .then(_ => message.success('Saved Lecture'))
        .catch(_ => message.error(''))
    }
  }
}

export default Form.create()(SubmitExam)

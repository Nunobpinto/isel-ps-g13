import React from 'react'
import { Form, Input, Button, TimePicker, Select, InputNumber } from 'antd'
import moment from 'moment'

const FormItem = Form.Item

class SubmitLecture extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      weekDay: undefined,
      begins: '',
      duration: '',
      location: '',
      createFlag: '',
      data: undefined
    }
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleDateChange = this.handleDateChange.bind(this)
  }

  handleDateChange (time, timeString) {
    this.setState({begins: timeString})
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.begins = this.state.begins
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
              <TimePicker defaultValue={moment('00:00', timeFormat)} format={timeFormat} onChange={this.handleDateChange}/>
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
      this.props.action(this.state.data)
      this.setState({createFlag: false})
    }
  }
}

export default Form.create()(SubmitLecture)

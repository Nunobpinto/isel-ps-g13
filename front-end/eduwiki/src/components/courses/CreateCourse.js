import React from 'react'
import { Form, Input, InputNumber, Button } from 'antd'

const FormItem = Form.Item

class CreateCourseForm extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      data: undefined,
      createCourseFlag: false
    }
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.setState({
          createCourseFlag: true,
          data: values
        })
      }
    })
  }
  render () {
    const { getFieldDecorator } = this.props.form
    return (
      <div id='formToCreateProgramme'>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            label='Full Name'
          >
            {getFieldDecorator('full_name', {
              rules: [{
                required: true, message: 'Please add a full Name to the new Course'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Short Name'
          >
            {getFieldDecorator('short_name', {
              rules: [{
                required: true, message: 'Please add a Short Name to the new Course'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Organization Id'
          >
            {getFieldDecorator('organization_id', {
              rules: [{
                required: true, message: 'Please add an organization id to the new Course'
              }]
            })(
              <InputNumber min={1} />
            )}
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Course</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.createCourseFlag) {
      this.props.action(this.state.data)
      this.setState({
        data: undefined,
        createProgrammeFlag: false
      })
    }
  }
}

export default Form.create()(CreateCourseForm)

import React from 'react'
import { Form, Input, InputNumber, Button } from 'antd'

const FormItem = Form.Item

class CreateProgrammeForm extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      data: undefined,
      createProgrammeFlag: false
    }
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.setState({
          createProgrammeFlag: true,
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
                required: true, message: 'Please add a full Name to the new Programme'
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
                required: true, message: 'Please add a Short Name to the new Programme'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Academic Degree'
          >
            {getFieldDecorator('academic_degree', {
              rules: [{
                required: true, message: 'Please add an Academic Degree to the new Programme'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Total Credits'
          >
            {getFieldDecorator('total_credits', {
              rules: [{
                required: true, message: 'Please add Total Credits to the new Programme'
              }]
            })(
              <InputNumber min={1} />
            )}
          </FormItem>
          <FormItem
            label='Duration'
          >
            {getFieldDecorator('duration', {
              rules: [{
                required: true, message: 'Please add Duration to the new Programme'
              }]
            })(
              <InputNumber min={1} />
            )}
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Programme</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.createProgrammeFlag) {
      this.props.action(this.state.data)
      this.setState({
        data: undefined,
        createProgrammeFlag: false
      })
    }
  }
}

export default Form.create()(CreateProgrammeForm)

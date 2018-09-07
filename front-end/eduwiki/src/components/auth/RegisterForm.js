import React from 'react'
import {Form, Input, Button} from 'antd'

class RegisterForm extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleConfirmBlur = this.handleConfirmBlur.bind(this)
    this.compareToFirstPassword = this.compareToFirstPassword.bind(this)
    this.validateToNextPassword = this.validateToNextPassword.bind(this)
    this.state = {
      username: '',
      password: '',
      email: '',
      personalEmail: '',
      givenName: '',
      register: false,
      current: 0,
      form: props.form
    }
  }

  handleConfirmBlur (e) {
    const value = e.target.value
    this.setState({ confirmDirty: this.state.confirmDirty || !!value })
  }

  compareToFirstPassword (rule, value, fn) {
    const form = this.state.form
    if (value && value !== form.getFieldValue('password')) {
      fn('Two passwords that you enter is inconsistent!')
    } else {
      fn()
    }
  }

  validateToNextPassword (rule, value, callback) {
    const form = this.state.form
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true })
    }
    callback()
  }

  handleSubmit (e) {
    e.preventDefault()
    this.state.form.validateFields((err, values) => {
      if (!err) {
        this.props.onPostSubmit(values, true)
      }
    })
  }

  render () {
    const { getFieldDecorator } = this.state.form
    return (
      <Form onSubmit={this.handleSubmit} className='login-form' >
        <Form.Item
          label='Username'
        >
          {getFieldDecorator('username', {
            rules: [{ required: true, message: 'Please input your username!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Organization Email'>
          {getFieldDecorator('email', {
            rules: [
              {type: 'email', message: 'The input is not valid E-mail!'},
              { required: true, message: 'Please input your organization email!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Password'>
          {getFieldDecorator('password', {
            rules: [
              {
                required: true, message: 'Please input your password!'
              },
              {
                validator: this.validateToNextPassword
              }
            ]
          })(
            <Input type='password' />
          )}
        </Form.Item>
        <Form.Item
          label='Confirm Password'
        >
          {getFieldDecorator('confirm', {
            rules: [{
              required: true, message: 'Please confirm your password!'
            }, {
              validator: this.compareToFirstPassword
            }]
          })(
            <Input type='password' onBlur={this.handleConfirmBlur} />
          )}
        </Form.Item>
        <Form.Item label='Family Name'>
          {getFieldDecorator('familyName', {
            rules: [{ required: true, message: 'Please input your family name!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Given Name'>
          {getFieldDecorator('givenName', {
            rules: [{ required: true, message: 'Please input your given name!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item >
          <Button type='primary' htmlType='submit' className='login-form-button'>Register</Button>
        </Form.Item>
      </Form>
    )
  }
}

const WrappedRegistrationForm = Form.create()(RegisterForm)
export default WrappedRegistrationForm

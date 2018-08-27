import React from 'react'
import {message, Form, Input, Button} from 'antd'
import fetcher from '../../fetcher'

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
      organizationEmail: '',
      personalEmail: '',
      familyName: '',
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
        <Form.Item
          label='Personal Email'
        >
          {getFieldDecorator('personalEmail', {
            rules: [
              {type: 'email', message: 'The input is not valid E-mail!'},
              { required: true, message: 'Please input your personal email!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Organization Email'>
          {getFieldDecorator('organizationEmail', {
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

  componentDidUpdate () {
    if (this.state.register) {
      const user = {
        username: this.state.username,
        family_name: this.state.familyName,
        personal_email: this.state.personalEmail,
        given_name: this.state.givenName,
        organization_email: this.state.organizationEmail,
        password: this.state.password
      }
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
          'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
        },
        body: JSON.stringify(user)
      }
      fetcher('http://localhost:8080/users', options)
        .then(_ => {
          this.setState(prevState => {
            const current = prevState.current + 1
            return ({
              register: false,
              current: current
            })
          })
        })
        .catch(error => {
          message.error(error.detail)
          this.setState({register: false})
        })
    }
  }
}

const WrappedRegistrationForm = Form.create()(RegisterForm)
export default WrappedRegistrationForm

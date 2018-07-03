import React from 'react'
import {message, Form, Input, Button} from 'antd'
import Cookies from 'universal-cookie'
import {Redirect} from 'react-router-dom'
const cookies = new Cookies()

class RegisterForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      email: '',
      familyName: '',
      givenName: '',
      redirect: false
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState(() => ({
          username: values.username,
          password: values.password,
          email: values.email,
          familyName: values.familyName,
          givenName: values.givenName,
          redirect: true
        }))
      }
    })
  }

  render () {
    if (cookies.get('auth')) {
      return (<Redirect to='/' />)
    }
    const { getFieldDecorator } = this.props.form
    return (
      <div id='holder'>
        <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
        <div style={{ padding: '30px' }}>
          <Form onSubmit={this.handleSubmit} className='login-form' >
            <Form.Item>
              {getFieldDecorator('username', {
                rules: [{ required: true, message: 'Please input your username!' }]
              })(
                <Input
                  name='username'
                  placeholder='Username'
                />
              )}
            </Form.Item>
            <Form.Item >
              {getFieldDecorator('email', {
                rules: [{ required: true, message: 'Please input your email!' }]
              })(
                <Input
                  name='email'
                  placeholder='Email'
                />
              )}
            </Form.Item>
            <Form.Item >
              {getFieldDecorator('password', {
                rules: [{ required: true, message: 'Please input your password!' }]
              })(
                <Input
                  name='password'
                  placeholder='Password'
                  type='password'
                />
              )}
            </Form.Item>
            <Form.Item >
              {getFieldDecorator('familyName', {
                rules: [{ required: true, message: 'Please input your family name!' }]
              })(
                <Input
                  name='familyName'
                  placeholder='Family Name'
                />
              )}
            </Form.Item>
            <Form.Item >
              {getFieldDecorator('givenName', {
                rules: [{ required: true, message: 'Please input your given name!' }]
              })(
                <Input
                  name='givenName'
                  placeholder='Given Name'
                  value={this.state.givenName} />
              )}
            </Form.Item>
            <Form.Item >
              <Button type='primary' htmlType='submit' className='login-form-button'>Register</Button>
            </Form.Item>
          </Form>
        </div>
      </div>
    )
  }
}

const WrappedRegistrationForm = Form.create()(RegisterForm)
export default WrappedRegistrationForm

import React from 'react'
import {Redirect, Link} from 'react-router-dom'
import {message, Form, Input, Button, Icon} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

class LoginForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      redirect: false,
      username: '',
      password: ''
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
          redirect: true
        }))
      }
    })
  }

  render () {
    if (cookies.get('auth')) {
      return (<Redirect to='' />)
    }
    const { getFieldDecorator } = this.props.form
    return (
      <div className='App'>
        <Form onSubmit={this.handleSubmit} className='login-form' id='formItem' >
          <Form.Item>
            {getFieldDecorator('username', {
              rules: [{ required: true, message: 'Please input your username!' }]
            })(
              <Input
                prefix={<Icon type='user' style={{ color: 'rgba(0,0,0,.25)' }} />}
                name='username'
                placeholder='Username'
                size='large'
                value={this.state.username} />
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('password', {
              rules: [{ required: true, message: 'Please input your Password!' }]
            })(
              <Input
                prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />}
                type='password'
                placeholder='Password'
                size='large'
                value={this.state.password} />
            )}
          </Form.Item>
          <Form.Item>
            <Button type='primary' htmlType='submit' className='login-form-button'>
              Log in
            </Button>
            <br />
                Or create an account
            <Link to='/register'> Register </Link>
          </Form.Item>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.redirect) {
      message.warning('Authentication not implemented yet')
      this.setState({redirect: false})
    }
  }
}

const WrappedNormalLoginForm = Form.create()(LoginForm)
export default WrappedNormalLoginForm

import React from 'react'
import {Redirect, Link} from 'react-router-dom'
import {message, Form, Input, Button, Icon} from 'antd'
import Cookies from 'universal-cookie'
import fetch from 'isomorphic-fetch'
const cookies = new Cookies()

class LoginForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      redirect: false,
      username: '',
      password: ''
    }
    if (this.props.destination) {
      message.warning('You need to be logged in to see what you wanted')
    }
    this.handleSubmit = this.handleSubmit.bind(this)
    this.tryLogin = this.tryLogin.bind(this)
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
      <div className='login_form'>
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
              />
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
                size='large' />
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

  tryLogin (authCredentials) {
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + authCredentials
      }
    }
    return new Promise((resolve, reject) => {
      fetch('http://localhost:8080/user', options)
        .then(resp => {
          if (resp.status >= 400) {
            return resp.json().then(error =>
              reject(error)
            )
          }
          return resolve(resp.json())
        })
        .catch(err => reject(err))
    })
  }

  componentDidUpdate () {
    if (this.state.redirect) {
      const credentials = Buffer.from(this.state.username + ':' + this.state.password).toString('base64')
      this.tryLogin(credentials)
        .then(_ => {
          cookies.set('auth', credentials, {maxAge: 9999})
          if (this.props.destination) {
            return this.props.history.push(this.props.destination.from.pathname)
          }
          this.props.history.push('/')
        })
        .catch(error => {
          message.error(error.detail)
          this.setState({redirect: false})
        })
    }
  }
}

const WrappedNormalLoginForm = Form.create()(LoginForm)
export default WrappedNormalLoginForm

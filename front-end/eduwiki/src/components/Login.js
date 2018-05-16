import React from 'react'
import {Redirect, Link} from 'react-router-dom'
import {message, Form, Input, Button, Icon, Spin} from 'antd'
import logo from '../logo.svg'
import 'antd/dist/antd.css'
import config from '../config'
import Cookies from 'universal-cookie'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
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
    let {redirect} = this.state
    if (cookies.get('auth')) {
      return (<Redirect to='' />)
    }
    if (redirect === true) {
      const authString = `${this.state.username}:${this.state.password}`
      const encoded = window.btoa(authString)
      const header = {
        method: 'GET',
        headers: {
          'Authorization': `Basic ${encoded}`,
          'Access-Control-Allow-Origin': '*'
        }
      }
      const url = config.API.PATH + '/api/users/' + this.state.username
      return (
        <HttpGet
          url={url}
          headers={header}
          render={(result) => (
            <div>
              <HttpGetSwitch
                result={result}
                onLoading={() => <div><Spin id='spin' tip='Checking user credentials...' /></div>}
                onJson={json => {
                  cookies.set('auth', encoded, {maxAge: 99999})
                  this.setState({redirect: false})
                  return (<Redirect to='/' />)
                }
                }
                onError={_ => {
                  message.error('Error in login, try again!')
                  this.setState({redirect: false})
                  return null
                }

                }
              />
            </div>
          )} />

      )
    }
    const { getFieldDecorator } = this.props.form
    return (
      <div className='App'>
        <header className='App-header'>
          <img src={logo} className='App-logo' alt='logo' />
          <h1 className='App-title'>Welcome to YACMA <small>(Yet Another Checklist Management Application)</small></h1>
        </header>
        <p className='App-intro' >
          <div>
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
        </p>
      </div>
    )
  }
}

const WrappedNormalLoginForm = Form.create()(LoginForm)
export default WrappedNormalLoginForm

import React from 'react'
import {Redirect} from 'react-router-dom'
import {message, Form, Input, Button, Icon} from 'antd'
import fetcher from '../fetcher'
import config from '../config'

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
    if (window.localStorage.getItem('auth')) {
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
          </Form.Item>
        </Form>
      </div>
    )
  }

  componentDidUpdate () {
    if (this.state.redirect) {
      const credentials = Buffer.from(this.state.username + ':' + this.state.password).toString('base64')
      const options = {
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Basic ' + credentials,
          'tenant-uuid': config.TENANT_UUID
        }
      }
      fetcher(config.API_PATH + '/user', options)
        .then(user => {
          if (user.reputation.role === 'ROLE_DEV') {
            window.localStorage.setItem('auth', credentials)
            if (this.props.destination) {
              message.success('Logged in')
              return this.props.history.push(this.props.destination.from.pathname)
            }
            this.props.history.push('/admin')
            this.setState({redirect: false})
          } else {
            message.error('No developer found with your credentials')
            this.setState({redirect: false})
          }
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

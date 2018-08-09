import React from 'react'
import {message, Form, Input, Button, Steps, Icon} from 'antd'
import Cookies from 'universal-cookie'
import {Redirect} from 'react-router-dom'
const cookies = new Cookies()
const Step = Steps.Step

class RegisterForm extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.registerForm = this.registerForm.bind(this)
    this.solutionIcon = () => (<Icon type='solution' />)
    this.waitingIcon = () => (<Icon type='loading' />)
    this.next = this.next.bind(this)
    this.prev = this.prev.bind(this)
    this.state = {
      username: '',
      password: '',
      email: '',
      familyName: '',
      givenName: '',
      redirect: false,
      current: 0,
      form: props.form,
      steps: [
        {
          title: 'Register',
          content: this.registerForm(),
          icon: this.solutionIcon()
        }, {
          title: 'Confirm',
          content: 'Ola',
          icon: this.waitingIcon()
        }, {
          title: 'Last',
          content: 'Last-content',
          icon: () => (
            <Icon type='smile-o' />
          )()
        }
      ]
    }
  }

  next () {
    const current = this.state.current + 1
    this.setState({ current })
  }

  prev () {
    const current = this.state.current - 1
    this.setState({ current })
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

  registerForm () {
    const { getFieldDecorator } = this.props.form
    return (
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
            />
          )}
        </Form.Item>
        <Form.Item >
          <Button type='primary' htmlType='submit' className='login-form-button'>Register</Button>
        </Form.Item>
      </Form>
    )
  }

  render () {
    if (cookies.get('auth')) {
      return (<Redirect to='/' />)
    }
    const { current } = this.state
    return (
      <div id='holder'>
        <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
        <div style={{ padding: '30px' }}>
          <Steps current={current}>
            {this.state.steps.map(item => <Step icon={item.icon} key={item.title} title={item.title} />)}
          </Steps>
          <div className='steps-content' id='centre_div'>
            {this.state.steps[current].content}
          </div>
          <div className='steps-action'>
            {
              current < this.state.steps.length - 1 &&
              <Button type='primary' onClick={() => this.next()}>Next</Button>
            }
            {
              current === this.state.steps.length - 1 &&
              <Button type='primary' onClick={() => message.success('Processing complete!')}>Done</Button>
            }
            {
              current > 0 &&
            (
              <Button style={{ marginLeft: 8 }} onClick={() => this.prev()}>
              Previous
              </Button>
            )
            }
          </div>
        </div>
      </div>
    )
  }
}

const WrappedRegistrationForm = Form.create()(RegisterForm)
export default WrappedRegistrationForm

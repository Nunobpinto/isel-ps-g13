import React from 'react'
import {message, Form, Input, Button, Steps, Icon} from 'antd'
import Cookies from 'universal-cookie'
import {Redirect} from 'react-router-dom'
import fetcher from '../../fetcher'
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
      organizationEmail: '',
      personalEmail: '',
      familyName: '',
      givenName: '',
      register: false,
      current: 0,
      form: props.form,
      steps: [
        {
          title: 'Register',
          content: this.registerForm(),
          icon: this.solutionIcon()
        }, {
          title: 'Confirm',
          content: 'Please check your email and follow the challenge',
          icon: this.waitingIcon()
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
          personalEmail: values.personal_email,
          familyName: values.familyName,
          givenName: values.givenName,
          organizationEmail: values.organization_email,
          register: true
        }))
      }
    })
  }

  registerForm () {
    const { getFieldDecorator } = this.props.form
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
          {getFieldDecorator('personal_email', {
            rules: [
              {type: 'email', message: 'The input is not valid E-mail!'},
              { required: true, message: 'Please input your personal email!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Organization Email'>
          {getFieldDecorator('organization_email', {
            rules: [
              {type: 'email', message: 'The input is not valid E-mail!'},
              { required: true, message: 'Please input your organization email!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Password'>
          {getFieldDecorator('password', {
            rules: [{ required: true, message: 'Please input your password!' }]
          })(
            <Input
              type='password'
            />
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

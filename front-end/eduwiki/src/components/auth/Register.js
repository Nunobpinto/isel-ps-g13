import React from 'react'
import {message, Button, Steps, Spin} from 'antd'
import RegisterForm from './RegisterForm'
import {Redirect} from 'react-router-dom'
import fetcher from '../../fetcher'
import config from '../../config'
const Step = Steps.Step

export default class Register extends React.Component {
  constructor (props) {
    super(props)
    this.registerForm = this.registerForm.bind(this)
    this.prev = this.prev.bind(this)
    this.state = {
      current: 0,
      steps: [
        {
          title: 'Register',
          content: this.registerForm()
        }, {
          title: 'Confirm',
          content: 'Please check your email and follow the challenge, after that login here'
        }
      ]
    }
  }

  prev () {
    const current = this.state.current - 1
    this.setState({ current })
  }

  registerForm () {
    return (
      <RegisterForm onPostSubmit={(values, register) => this.setState({
        values: values,
        register: register
      })} />
    )
  }

  componentDidUpdate () {
    if (this.state.register) {
      const user = {
        username: this.state.values.username,
        family_name: this.state.values.familyName,
        email: this.state.values.email,
        given_name: this.state.values.givenName,
        password: this.state.values.password
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
      fetcher(config.API_PATH + '/users', options)
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
    if (window.localStorage.getItem('auth')) {
      return (<Redirect to='/' />)
    }
    const { current } = this.state
    return (
      <div id='holder'>
        <img alt='EduWiki Logo' id='home-logo' src='logo_color.png' />
        <div style={{ padding: '30px' }}>
          {this.state.register && <Spin tip='Submiting your regist' />}
          <Steps current={current}>
            {this.state.steps.map(item => <Step key={item.title} title={item.title} />)}
          </Steps>
          <div className='steps-content' id='centre_div'>
            {this.state.steps[current].content}
          </div>
          <div className='steps-action'>
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

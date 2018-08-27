import React from 'react'
import {Form, Input, Button, message} from 'antd'
import fetch from 'isomorphic-fetch'

const { TextArea } = Input

class TenantCreator extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.userForm = this.userForm.bind(this)
    this.state = {
      fullName: '',
      shortName: '',
      address: '',
      contact: '',
      website: '',
      emailPattern: '',
      organizationSummary: '',
      requesters: []
    }
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const requesters = [
          {
            username: values.pioneer_username,
            family_name: values.pioneer_familyName,
            given_name: values.pioneer_givenName,
            organization_email: values.pioneer_organizationEmail,
            principal: true
          },
          {
            username: values.first_guest_username,
            family_name: values.first_guest_familyName,
            given_name: values.first_guest_givenName,
            organization_email: values.first_guest_organizationEmail,
            principal: false
          },
          {
            username: values.second_guest_username,
            family_name: values.second_guest_familyName,
            given_name: values.second_guest_givenName,
            organization_email: values.second_guest_organizationEmail,
            principal: false
          }
        ]
        const data = {
          full_name: values.fullName,
          short_name: values.shortName,
          address: values.address,
          contact: values.contact,
          website: values.website,
          email_pattern: values.emailPattern,
          organization_summary: values.organizationSummary,
          requesters: requesters
        }
        this.setState({
          submit: true,
          data: data
        })
      }
    })
  }
  userForm (user, getFieldDecorator) {
    return (
      <div>
        <Form.Item
          label='Username'
        >
          {getFieldDecorator(`${user}_username`, {
            rules: [{ required: true, message: 'Please input your username!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Organization Email'>
          {getFieldDecorator(`${user}_organizationEmail`, {
            rules: [
              {type: 'email', message: 'The input is not valid E-mail!'},
              { required: true, message: 'Please input your organization email!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Family Name'>
          {getFieldDecorator(`${user}_familyName`, {
            rules: [{ required: true, message: 'Please input your family name!' }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item label='Given Name'>
          {getFieldDecorator(`${user}_givenName`, {
            rules: [{ required: true, message: 'Please input your given name!' }]
          })(
            <Input />
          )}
        </Form.Item>
      </div>
    )
  }
  render () {
    const { getFieldDecorator } = this.props.form
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      }
    }
    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0
        },
        sm: {
          span: 16,
          offset: 8
        }
      }
    }
    return (
      <Form onSubmit={this.handleSubmit}>
        <p>
          Tell us about your organization :
        </p>
        <Form.Item
          {...formItemLayout}
          label='Email Pattern'
        >
          {getFieldDecorator('emailPattern', {
            rules: [{
              required: true, message: 'Please input the Email Pattern of your organization'
            }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Organization Full Name'
        >
          {getFieldDecorator('fullName', {
            rules: [{ required: true, message: 'Please tell us your Organization Full Name', whitespace: true }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Organization Short Name'
        >
          {getFieldDecorator('shortName', {
            rules: [{ required: true, message: 'Please tell us your Organization Short Name', whitespace: true }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Address'
        >
          {getFieldDecorator('address', {
            rules: [{ required: true, message: 'Please tell us your Organization Address', whitespace: true }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Contact'
        >
          {getFieldDecorator('contact', {
            rules: [{ required: true, message: 'Please tell us your Organization Contact', whitespace: true }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Organization Website'
        >
          {getFieldDecorator('website', {
            rules: [{ required: true, message: 'Please tell us your Organization Website', whitespace: true }]
          })(
            <Input />
          )}
        </Form.Item>
        <Form.Item
          {...formItemLayout}
          label='Describe your Organization'
        >
          {getFieldDecorator('organizationSummary', {
            rules: [{ required: true, message: 'Please tell us about your Organization', whitespace: true }]
          })(
            <TextArea />
          )}
        </Form.Item>
        <p>
          Now please gather 4 more people and write here their information,
              so that all of you become administrators in your newly tenant created for your organization.
        </p>
        <p>
              Describe your own information, you'll be the Pioneer user, the devs will reach you for every information they need
        </p>
        <p>Pioneer</p>
        {this.userForm('pioneer', getFieldDecorator)}
        <p>First Guest</p>
        {this.userForm('first_guest', getFieldDecorator)}
        <p>Second Guest</p>
        {this.userForm('second_guest', getFieldDecorator)}
        <Form.Item {...tailFormItemLayout}>
          <Button type='primary' htmlType='submit'>submit</Button>
        </Form.Item>
      </Form>
    )
  }
  componentDidUpdate () {
    if (this.state.submit) {
      const uri = 'http://localhost:8080/pending'
      const options = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.state.data)
      }
      fetch(uri, options)
        .then(resp => {
          if (resp.status >= 400) {
            throw new Error()
          }
          return resp.json()
        })
        .then(_ => {
          this.props.successCb()
          this.setState({submit: false})
        })
        .catch(() => message.error('Error submiting your data'))
    }
  }
}

const WrappedForm = Form.create()(TenantCreator)
export default WrappedForm

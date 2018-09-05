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
            username: values.principal_username,
            family_name: values.principal_familyName,
            given_name: values.principal_givenName,
            organization_email: values.principal_organizationEmail,
            principal: true
          },
          {
            username: values.user1_username,
            family_name: values.user1_familyName,
            given_name: values.user1_givenName,
            organization_email: values.user1_organizationEmail,
            principal: false
          },
          {
            username: values.user2_username,
            family_name: values.user2_familyName,
            given_name: values.user2_givenName,
            organization_email: values.user2_organizationEmail,
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
          Now please gather 2 more people and write here their information,
              so that all of you become administrators in your newly tenant created for your organization.
        </p>
        <p>
              Describe your own information, you'll be the Principal user, the devs will reach you for every information they need
        </p>
        <p>Principal</p>
        {this.userForm('principal', getFieldDecorator)}
        <p>Other User 1</p>
        {this.userForm('user1', getFieldDecorator)}
        <p>Other User 2</p>
        {this.userForm('user2', getFieldDecorator)}
        <Form.Item {...tailFormItemLayout}>
          <Button type='primary' htmlType='submit'>submit</Button>
        </Form.Item>
      </Form>
    )
  }
  componentDidUpdate () {
    if (this.state.submit) {
      const uri = 'http://localhost:8080/tenants/pending'
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

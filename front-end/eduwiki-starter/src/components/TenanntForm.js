import React from 'react'
import Layout from './Layout'
import fetch from 'isomorphic-fetch'
import {Form, Input, Button} from 'antd'

const {FormItem} = Form
const { TextArea } = Input

class TenantCreator extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
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
      <Layout history={this.props.history}>
        <p>Tell us about your organization</p>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            {...formItemLayout}
            label='E-mail'
          >
            {getFieldDecorator('email', {
              rules: [{
                type: 'email', message: 'The input is not valid E-mail!'
              }, {
                required: true, message: 'Please input your E-mail!'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Organization Full Name'
          >
            {getFieldDecorator('fullName', {
              rules: [{ required: true, message: 'Please tell us your Organization Full Name', whitespace: true }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Organization Short Name'
          >
            {getFieldDecorator('shortName', {
              rules: [{ required: true, message: 'Please tell us your Organization Short Name', whitespace: true }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Address'
          >
            {getFieldDecorator('address', {
              rules: [{ required: true, message: 'Please tell us your Organization Address', whitespace: true }]
            })(
              <TextArea />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Contact'
          >
            {getFieldDecorator('contact', {
              rules: [{ required: true, message: 'Please tell us your Organization Contact', whitespace: true }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Organization Website'
          >
            {getFieldDecorator('website', {
              rules: [{ required: true, message: 'Please tell us your Organization Website', whitespace: true }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label='Describe your Organization'
          >
            {getFieldDecorator('organizationSummary', {
              rules: [{ required: true, message: 'Please tell us about your Organization', whitespace: true }]
            })(
              <TextArea />
            )}
          </FormItem>
          <p>Now please gather 4 more people and write here their information, 
              so that all of you become administrators in your newly tenant created for your organization.
          </p>
          <p>
              Describe your own information, you'll be the Pioneer user, the devs will reach you for every information they need
          </p>
          <FormItem {...tailFormItemLayout}>
            <Button type='primary' htmlType='submit'>Register</Button>
          </FormItem>
        </Form>
      </Layout>

    )
  }
}

const TenantForm = Form.create()(TenantCreator)
export default TenantForm

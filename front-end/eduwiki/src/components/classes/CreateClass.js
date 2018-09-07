import React from 'react'
import fetcher from '../../fetcher'
import { Form, Input, Button, Select } from 'antd'
import config from '../../config'

const FormItem = Form.Item

class CreateCourseForm extends React.Component {
  constructor (props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.state = {
      data: undefined,
      createCourseFlag: false,
      programmes: [],
      terms: []
    }
  }
  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.setState({
          createClassFlag: true,
          data: values
        })
      }
    })
  }
  render () {
    const { getFieldDecorator } = this.props.form
    return (
      <div id='formToCreateProgramme'>
        <Form onSubmit={this.handleSubmit}>
          <FormItem
            label='Class Name'
          >
            {getFieldDecorator('class_name', {
              rules: [{
                required: true, message: 'Please add a name to the new Class'
              }]
            })(
              <Input />
            )}
          </FormItem>
          <FormItem
            label='Term'
          >
            {getFieldDecorator('term_id', {
              rules: [{
                required: true, message: 'Please choose a Term'
              }]
            })(
              <Select>
                {this.state.terms.map(term => (
                  <Select.Option value={term.termId}>{term.shortName}</Select.Option>
                ))}
              </Select>
            )}
          </FormItem>
          <FormItem
            label='Programme'
          >
            {getFieldDecorator('programme_id', {
              rules: [{
                required: true, message: 'Please choose a Programme'
              }]
            })(
              <Select>
                {this.state.programmes.map(programme => (
                  <Select.Option value={programme.programmeId}>{programme.shortName}</Select.Option>
                ))}
              </Select>
            )}
          </FormItem>
          <FormItem>
            <Button type='primary' htmlType='submit'>Create Course</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
  componentDidUpdate () {
    if (this.state.createClassFlag) {
      this.props.action(this.state.data)
      this.setState({
        data: undefined,
        createClassFlag: false
      })
    }
  }
  componentDidMount () {
    const termsUrl = config.API_PATH + '/terms'
    const programmesUrl = config.API_PATH + '/programmes'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + this.props.auth,
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(termsUrl, options)
      .then(json => {
        const terms = json.termList
        fetcher(programmesUrl, options)
          .then(progJson => {
            const programmes = progJson.programmeList
            this.setState({
              terms: terms,
              programmes: programmes
            })
          })
      })
  }
}

export default Form.create()(CreateCourseForm)

import React from 'react'
import {Button, Input, InputNumber, Form, Select} from 'antd'
import timestampParser from '../../timestampParser'

const FormItem = Form.Item

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      optional: undefined,
      credits: undefined,
      lectured_term: undefined
    }
    this.onChange = this.onChange.bind(this)
    this.onOptionalChange = this.onOptionalChange.bind(this)
    this.onCreditsChange = this.onCreditsChange.bind(this)
    this.checkInputs = this.checkInputs.bind(this)
    this.addCourseToProgramme = this.props.addCourse
  }

  onChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  onOptionalChange (value) {
    this.setState({
      'optional': value.toLowerCase()
    })
  }

  onCreditsChange (value) {
    this.setState({
      'credits': value
    })
  }

  checkInputs () {
    return this.state.lectured_term && this.state.optional && this.state.credits
  }

  render () {
    const {crs} = this.props
    return (
      <div>
        <p>{crs.fullName} - <small>{timestampParser(crs.timestamp)}</small> </p>
        <p>Created By : <a href={`/users/${crs.createdBy}`}>{crs.createdBy}</a></p>
        <Form>
          <FormItem label='Lectured Term'>
            <Input
              placeholder='choose term'
              name='lectured_term'
              onChange={this.onChange}
            />
          </FormItem>
          <FormItem label='Optional'>
            <Select
              showSearch
              style={{ width: 200 }}
              placeholder='Choose Optional Or Mandatory'
              optionFilterProp='children'
              onChange={this.onOptionalChange}
            >
              <Select.Option value='Optional'>Optional</Select.Option>
              <Select.Option value='Mandatory'>Mandatory</Select.Option>
            </Select>
          </FormItem>
          <FormItem label='Credits' >
            <InputNumber
              placeholder='Choose Credits of this Course on this programme'
              onChange={this.onCreditsChange}
            />
          </FormItem>
          <FormItem>
            <Button
              type='primary'
              disabled={!this.checkInputs()}
              key={crs.id}
              onClick={() => {
                this.setState({addCourseToProgramme: true})
              }}>
                        Add To Programme
            </Button>
          </FormItem>
        </Form>
      </div>
    )
  }

  componentDidUpdate () {
    if (this.state.addCourseToProgramme) {
      const course = {
        lectured_term: this.state.lectured_term,
        optional: this.state.optional === 'optional',
        credits: this.state.credits,
        course_id: this.props.crs.courseId
      }
      this.addCourseToProgramme(course)
      this.setState({addCourseToProgramme: false})
    }
  }
}

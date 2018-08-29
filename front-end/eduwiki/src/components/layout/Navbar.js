import React from 'react'
import TransparentButton from '../comms/TransparentButton'
import { Avatar, message, Row, Col } from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      points: 0,
      role: ''
    }
  }
  render () {
    return (
      <div>
        <Row gutter={16}>
          <Col span={6}>
            <a href='/'>
              <img
                className='app_icon'
                src='/logo.png'
                alt='EduWiki Logo' />
            </a>
            <TransparentButton
              destiny='/user'
              message={`${this.props.user.username} (${this.props.user.reputation.role} - ${this.props.user.reputation.points} points)`} />
          </Col>
          <Col span={6} />
          <Col span={6} />
          <Col span={6}>
            <div className='right_side'>
              <TransparentButton
                className=''
                destiny='/logout'
                message={'Logout'}
              />
            </div>
          </Col>
        </Row>
      </div>
    )
  }
}

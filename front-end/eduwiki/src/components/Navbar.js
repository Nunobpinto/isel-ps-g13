import React from 'react'
import TransparentButton from './TransparentButton'
import { Dropdown, Menu, Avatar, message, Row, Col } from 'antd'
import fetch from 'isomorphic-fetch'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const menu = (
  <Menu>
    <Menu.Item>
      <a href='/user'>My Page</a>
    </Menu.Item>
  </Menu>
)

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: ''
    }
  }
  render () {
    return (
      <div>
        <Row gutter={16}>
          <Col span={6}>
            <img className='app_icon' src='/logo.png' alt='EduWiki Logo' />
            <TransparentButton
              destiny='/user'
              message={this.state.username} />
          </Col>
          <Col span={6} />
          <Col span={6} />
          <Col span={6}>
            <TransparentButton
              destiny='/logout'
              message={'Logout'}
            />
          </Col>
        </Row>
      </div>
    )
  }
  componentDidMount () {
    const auth = cookies.get('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth
      }
    }
    fetch('http://localhost:8080/user', options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('error!!!')
        }
        return resp.json()
      })
      .then(json => this.setState({username: json.username}))
      .catch(_ => message.error('Something bad happened'))
  }
}

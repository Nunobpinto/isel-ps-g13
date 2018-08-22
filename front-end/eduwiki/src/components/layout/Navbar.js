import React from 'react'
import TransparentButton from '../comms/TransparentButton'
import { Avatar, message, Row, Col } from 'antd'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

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
            <a href='/'>
              <img
                className='app_icon'
                src='/logo.png'
                alt='EduWiki Logo' />
            </a>
            <TransparentButton
              destiny='/user'
              message={this.state.username} />
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
  componentDidMount () {
    const auth = cookies.get('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user', options)
      .then(json => this.setState({username: json.username}))
      .catch(_ => message.error('Something bad happened'))
  }
}

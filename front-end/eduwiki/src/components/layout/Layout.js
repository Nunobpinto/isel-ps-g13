import React from 'react'
import { Layout, message } from 'antd'
import Navbar from './Navbar'
import fetcher from '../../fetcher'
import config from '../../config'
const { Header, Content, Footer } = Layout

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      user: {
        username: '',
        reputation: {
          role: '',
          points: ''
        }
      }
    }
  }
  render () {
    const { children } = this.props
    const childrenWithProps = React.Children.map(children, child =>
      React.cloneElement(child, { user: this.state.user }))
    return (
      <Layout>
        <Header id='navbar'>
          {React.cloneElement(<Navbar />, { user: this.state.user })}
        </Header>
        <Content className='layout'>
          <div style={{ background: '#fff', padding: 30, minHeight: 1000 }}>
            {childrenWithProps}
          </div>
        </Content>
        <Footer className='footer' style={{ textAlign: 'center' }}>
            Eduwiki - Final Project 2018
        </Footer>
      </Layout>
    )
  }
  componentDidMount () {
    const auth = window.localStorage.getItem('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/user', options)
      .then(json => this.setState({
        user: json
      }))
      .catch(error => message.error(error.detail))
  }
}

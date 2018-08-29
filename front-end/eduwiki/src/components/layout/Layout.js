import React from 'react'
import { Layout, message } from 'antd'
import Navbar from './Navbar'
import fetcher from '../../fetcher'
import Cookies from 'universal-cookie'
const cookies = new Cookies()
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
          <div style={{ background: '#fff', padding: 57, minHeight: 280 }}>
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
    const auth = cookies.get('auth')
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + auth,
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user', options)
      .then(json => this.setState({
        user: json
      }))
      .catch(_ => message.error('Something bad happened'))
  }
}

import React from 'react'
import { Layout, Menu, Button } from 'antd'
import { NavLink } from 'react-router-dom'
const { Header, Content, Footer } = Layout

export default(props) => (
  <Layout className='layout'>
    <Header id='navbar'>
      <div className='logo' />
      <Menu
        theme='dark'
        mode='horizontal'
        style={{ lineHeight: '64px' }}
      >
        <Menu.Item as={NavLink} exact to={'/home'}>
          <Button
            type='default'
            ghost
            onClick={() => {
              props.history.push('/home')
            }}>
                Home
          </Button>
        </Menu.Item>
        <Menu.Item>
          <Button
            type='default'
            ghost
            onClick={() => {
              props.history.push('/create-tenant')
            }}
          >
                Create Tenant
          </Button>
        </Menu.Item>
      </Menu>
    </Header>
    <Content style={{ padding: '2%' }}>
      <div style={{ background: '#fff', padding: 24, minHeight: 280 }}>
        {props.children}
      </div>
    </Content>
    <Footer className='footer' style={{ textAlign: 'center' }}>
            Eduwiki - Final Project 2018
    </Footer>
  </Layout>
)

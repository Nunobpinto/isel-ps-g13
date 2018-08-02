import React from 'react'
import { Menu, Layout, Dropdown, Avatar } from 'antd'
const { Header, Content, Footer } = Layout

const menu = (
  <Menu>
    <Menu.Item>
      Hello there!!
    </Menu.Item>
  </Menu>
)

export default (props) => (
  <Layout>
    <Header style={{position: 'fixed', zIndex: 1, width: '100%'}} id='navbar'>
      <div className='logo'>
        <img src='/logo.png' width='7%' alt='EduWiki Logo' />
        <Dropdown overlay={menu} trigger={['click']}>
          <Avatar style={{ backgroundColor: '#99ff33' }} icon='user' />
        </Dropdown>
      </div>
    </Header>
    <Content className='layout'>
      <div style={{ background: '#fff', padding: 57, minHeight: 280 }}>
        {props.children}
      </div>
    </Content>
    <Footer className='footer' style={{ textAlign: 'center' }}>
            Eduwiki - Final Project 2018
    </Footer>
  </Layout>
)

import React from 'react'
import { Menu, Layout, Dropdown, Avatar, Button } from 'antd'
import Navbar from './Navbar'
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
    <Header id='navbar'>
      <Navbar />
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

import React from 'react'
import { Menu, Layout } from 'antd'
const { Header, Content, Footer } = Layout

export default (props) => (
  <Layout className='layout'>
    <Header id='navbar'>
      <div className='logo'>
        <img src='/logo.png' width='10%' />
      </div>
      <Menu
        theme='light'
        mode='horizontal'
      />
    </Header>
    <Content>
      <div style={{ background: '#fff', padding: 24, minHeight: 280 }}>
        {props.component}
      </div>
    </Content>
    <Footer className='footer' style={{ textAlign: 'center' }}>
            Eduwiki - Final Project 2018
    </Footer>
  </Layout>
)

import React from 'react'
import { Menu, Layout } from 'antd'
const { Header, Content, Footer } = Layout

export default (props) => (
  <Layout>
    <Header style={{position: 'fixed', zIndex: 1, width: '100%'}} id='navbar'>
      <div className='logo'>
        <img src='/logo.png' width='7%' alt='EduWiki Logo' />
      </div>
      <Menu
        theme='light'
        mode='horizontal'
      />
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

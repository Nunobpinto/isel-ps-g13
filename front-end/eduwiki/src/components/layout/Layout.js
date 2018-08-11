import React from 'react'
import { Layout } from 'antd'
import Navbar from './Navbar'
const { Header, Content, Footer } = Layout

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

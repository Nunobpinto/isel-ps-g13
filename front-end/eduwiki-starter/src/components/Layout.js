import React from 'react'
import { Layout, Button, Row, Col } from 'antd'
const { Header, Content, Footer } = Layout

export default(props) => (
  <Layout className='layout'>
    <Header id='navbar'>
      <div className='logo' />
      <Row gutter={16}>
        <Col className='gutter-row' span={6}>
          <Button
            type='default'
            ghost
            onClick={() => {
              props.history.push('/home')
            }}>
                Home
          </Button>
        </Col>
        <Col className='gutter-row' span={6}>
          <Button
            type='default'
            ghost
            onClick={() => {
              props.history.push('/create-tenant')
            }}
          >
                Create Tenant
          </Button>
        </Col>
        {window.localStorage.getItem('auth')
          ? <div>
            <Col className='gutter-row' span={6}>
              <Button
                type='default'
                ghost
                onClick={() => {
                  props.history.push('/admin')
                }}
              >
              Admin Page
              </Button>
            </Col>
            <Col className='gutter-row' span={6}>
              <Button
                type='default'
                ghost
                onClick={() => {
                  props.history.push('/logout')
                }}
              >
              Sign Out
              </Button>
            </Col>
          </div>
          : <Col className='gutter-row' span={6}>
            <Button
              type='default'
              ghost
              onClick={() => {
                props.history.push('/login')
              }}
            >
            Admin Login
            </Button>
          </Col>
        }
      </Row>
    </Header>
    <Content>
      <div style={{ background: '#fff', padding: 24, minHeight: 500 }}>
        {props.children}
      </div>
    </Content>
    <Footer className='footer' style={{ textAlign: 'center' }}>
            Eduwiki - Final Project 2018
    </Footer>
  </Layout>
)

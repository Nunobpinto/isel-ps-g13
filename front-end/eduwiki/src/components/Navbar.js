import React from 'react'
import { withRouter, NavLink } from 'react-router-dom'
import { Layout, Menu, Button } from 'antd'

const { Header } = Layout

export default withRouter(({history}) => (
  <Layout className='layout'>
    <Header className='header'>
      <div className='logo' />
      <Menu
        theme='dark'
        mode='horizontal'
        style={{ lineHeight: '64px' }}
      >
        <Menu.Item as={NavLink} exact to={'/'}>
          <Button
            type='default'
            ghost
            onClick={() => {
              history.push('/')
            }}>
                YAMCA
          </Button>
        </Menu.Item>
        <Menu.Item>
          <Button
            type='default'
            ghost
            onClick={() => {
              history.push('/logout')
            }}
          >
                Sign Out
          </Button>
        </Menu.Item>
        <Menu.Item as={NavLink} exact to={'/checklists'}>
          <Button
            type='default' ghost
            onClick={() => {
              history.push('/checklists')
            }}t>
                Manage My Checklists
          </Button>
        </Menu.Item>
        <Menu.Item as={NavLink} exact to={'/templates'}>
          <Button
            type='default'
            ghost
            onClick={() => {
              history.push('/templates')
            }}>
                See All My Templates
          </Button>
        </Menu.Item>
      </Menu>
    </Header>
  </Layout>
)
)
